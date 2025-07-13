package it.uniroma3.siw.siwbooks.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.dto.NuovoAutoreDTO;
import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.repository.AutoreRepository;
import it.uniroma3.siw.siwbooks.repository.LibroRepository;



@Service
public class AutoreService {

    @Autowired
    AutoreRepository autoreRepository;
    @Autowired 
    LibroRepository libroRepository;

    public List<Autore> getAllAutori(){
        return StreamSupport.stream(autoreRepository.findAll().spliterator(), false).toList();
    }

    public Autore getAutoreById(Long id) {
        return autoreRepository.findById(id).orElse(null);
    }

    public void salvaNuovoAutore(NuovoAutoreDTO nuovoAutoreDTO, Immagine immagine) {
        Autore autore = new Autore(
        nuovoAutoreDTO.getNome(),
        nuovoAutoreDTO.getCognome(),
        nuovoAutoreDTO.getNazionalita(),
        nuovoAutoreDTO.getDataNascita(),
        nuovoAutoreDTO.getDataMorte(),
        immagine);
        autoreRepository.save(autore);
    }

    public void rimuoviLibroDaAutore(Long autoreId, Long libroId) {
        Autore autore = autoreRepository.findById(autoreId).orElse(null);
        autore.getLibri().remove(libroRepository.findById(libroId).orElse(null));
        libroRepository.findById(libroId).ifPresent(libro -> {
            libro.getAutori().remove(autore);
            libroRepository.save(libro);
        });
        autoreRepository.save(autore);
    }

    public void modifcaAutore(Autore autore, NuovoAutoreDTO dto, Immagine immagine) {
        // Usa una funzione helper per controllare se una stringa è valida
        if(isValidString(dto.getNome())) {
            autore.setNome(dto.getNome().trim());
        }
        if(isValidString(dto.getCognome())) {
            autore.setCognome(dto.getCognome().trim());
        }
        if(isValidString(dto.getNazionalita())) {
            autore.setNazionalita(dto.getNazionalita().trim());
        }
        if(isValidString(dto.getDataNascita())) {
            autore.setDataNascita(dto.getDataNascita().trim());
        }
        if(isValidString(dto.getDataMorte())) {
            autore.setDataMorte(dto.getDataMorte().trim());
        }
        if(immagine != null){
            autore.setImmagine(immagine);
        }
        
        // Verifica finale prima del save
        if (autore.getNome() == null || autore.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome dell'autore non può essere vuoto");
        }
        if (autore.getCognome() == null || autore.getCognome().trim().isEmpty()) {
            throw new RuntimeException("Cognome dell'autore non può essere vuoto");
        }
        if (autore.getNazionalita() == null || autore.getNazionalita().trim().isEmpty()) {
            throw new RuntimeException("Nazionalità dell'autore non può essere vuota");
        }
        if (autore.getDataNascita() == null || autore.getDataNascita().trim().isEmpty()) {
            throw new RuntimeException("Data di nascita dell'autore non può essere vuota");
        }
        
        autoreRepository.save(autore);
    }
    
    // Metodo helper per controllare se una stringa è valida
    private boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public void rimuoviDataMorte(Autore autore) {
        autore.setDataMorte(null);
        autoreRepository.save(autore);
    }

}
