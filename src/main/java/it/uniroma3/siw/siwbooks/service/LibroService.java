package it.uniroma3.siw.siwbooks.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.dto.NuovoLibroDTO;
import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.repository.AutoreRepository;
import it.uniroma3.siw.siwbooks.repository.LibroRepository;

@Service
public class LibroService {

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

}
