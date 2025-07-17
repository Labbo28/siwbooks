package it.uniroma3.siw.siwbooks.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import it.uniroma3.siw.siwbooks.repository.RecensioneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.siwbooks.controller.RecensioneController;
import it.uniroma3.siw.siwbooks.dto.NuovaImmagineDTO;
import it.uniroma3.siw.siwbooks.dto.NuovoLibroDTO;
import it.uniroma3.siw.siwbooks.dto.TipoImmagine;
import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.model.Recensione;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.repository.AutoreRepository;
import it.uniroma3.siw.siwbooks.repository.LibroRepository;

@Service
public class LibroService {


    @Autowired
    private ImmagineService immagineService;


    @Autowired
    private  RecensioneRepository recensioneRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutoreRepository autoreRepository;

  

    public List<Libro> getAllLibri() {
        return StreamSupport.stream(libroRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }

    public Libro getLibroById(Long id) {
        return libroRepository.findById(id).orElse(null);
    }

    /**
     * Salva un nuovo libro, associa gli autori e lo persiste in DB.
     */
    public Libro salvaNuovoLibro(NuovoLibroDTO nuovoLibroDTO, List<Immagine> immaginiCopertina) {
        // 1) Crea l'entità Libro e imposta i dati base
        Libro nuovoLibro = new Libro();
        nuovoLibro.setTitolo(nuovoLibroDTO.getTitolo());
        nuovoLibro.setAnnoPubblicazione(nuovoLibroDTO.getAnnoPubblicazione());

        // 2) Se ci sono idAutore, recuperali e associaci gli Autori
        List<Long> idAutori = nuovoLibroDTO.getIdAutore();
        if (idAutori != null && !idAutori.isEmpty()) {
            Set<Autore> autori = idAutori.stream()
                .map(autoreRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            nuovoLibro.setAutori(autori);
        }
        nuovoLibro.setCopertina(immaginiCopertina);

        // 3) Salva il libro nel repository
        return libroRepository.save(nuovoLibro);
    }

    public List<Libro> cercaLibri(String query) {
    return libroRepository.findByTitoloIgnoreCaseContaining(query);
}

  public void deleteLibro(Long id) {
    Optional<Libro> opt = libroRepository.findById(id);
    
    // Verifica se il libro esiste
    if (!opt.isPresent()) {
        throw new EntityNotFoundException("Libro con ID " + id + " non trovato");
    }
    
    Libro toDelete = opt.get();
    
    // Gestione delle immagini di copertina
    if (toDelete.getCopertina() != null) {
        for (Immagine i : toDelete.getCopertina()) {
            i.setUnlinked(true);
        }
    }
    
    // Rimozione delle associazioni con gli autori
    if (toDelete.getAutori() != null) {
        for (Autore a : toDelete.getAutori()) {
            a.getLibri().remove(toDelete);
            autoreRepository.save(a);
        }
    }
    
    // Gestione delle recensioni
    if (toDelete.getRecensioni() != null) {
        for (Recensione r : toDelete.getRecensioni()) {
            // Rimuovi la recensione dall'utente proprietario
            if (r.getUtente() != null) {
                r.getUtente().getRecensioni().remove(r);
            }
            recensioneRepository.delete(r);
        }
    }
    
    // Elimina il libro
    libroRepository.delete(toDelete);
}


@Transactional
public void modificaLibro(Long id, NuovoLibroDTO nuovoLibroDTO) {
    // Recupera il libro esistente
    Libro libro = libroRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + id + " non trovato"));
    
    // Aggiorna i campi base
    libro.setTitolo(nuovoLibroDTO.getTitolo());
    libro.setAnnoPubblicazione(nuovoLibroDTO.getAnnoPubblicazione());
    
    // Gestione degli autori
    aggiornaAutoriLibro(libro, nuovoLibroDTO.getIdAutore());
    
    // Gestione delle immagini
    if (nuovoLibroDTO.getFileImmagine() != null && !nuovoLibroDTO.getFileImmagine().isEmpty()) {
        aggiornaImmaginiLibro(libro, nuovoLibroDTO.getFileImmagine());
    }
    
    // Salva il libro modificato
    libroRepository.save(libro);
}

private void aggiornaAutoriLibro(Libro libro, List<Long> nuoviIdAutori) {
    // Rimuovi il libro dagli autori che non sono più associati
    Set<Autore> autoriAttuali = new HashSet<>(libro.getAutori());
    for (Autore autore : autoriAttuali) {
        if (nuoviIdAutori == null || !nuoviIdAutori.contains(autore.getId())) {
            autore.getLibri().remove(libro);
            libro.getAutori().remove(autore);
        }
    }
    
    // Aggiungi nuovi autori
    if (nuoviIdAutori != null) {
        for (Long idAutore : nuoviIdAutori) {
            if (libro.getAutori().stream().noneMatch(a -> a.getId().equals(idAutore))) {
                Autore autore = autoreRepository.findById(idAutore)
                    .orElseThrow(() -> new EntityNotFoundException("Autore con ID " + idAutore + " non trovato"));
                
                libro.getAutori().add(autore);
                autore.getLibri().add(libro);
            }
        }
    }
}

private void aggiornaImmaginiLibro(Libro libro, List<MultipartFile> nuoveImmagini) {
    try {
        // Marca le immagini attuali come unlinked
        if (libro.getCopertina() != null) {
            for (Immagine immagine : libro.getCopertina()) {
                immagine.setUnlinked(true);
            }
        }
        
        // Pulisce la lista delle immagini
        libro.getCopertina().clear();
        
        // Processa le nuove immagini
        for (MultipartFile file : nuoveImmagini) {
            if (!file.isEmpty()) {
                NuovaImmagineDTO immagineDTO = new NuovaImmagineDTO(file, TipoImmagine.COPERTINA);
                Immagine nuovaImmagine = immagineService.uploadImmagine(immagineDTO);
                libro.getCopertina().add(nuovaImmagine);
            }
        }
    } catch (Exception e) {
        throw new RuntimeException("Errore durante l'aggiornamento delle immagini: " + e.getMessage(), e);
    }
}
}
