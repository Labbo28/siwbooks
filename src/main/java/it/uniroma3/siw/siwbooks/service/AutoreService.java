package it.uniroma3.siw.siwbooks.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.dto.NuovoAutoreDTO;
import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.repository.AutoreRepository;



@Service
public class AutoreService {

    @Autowired
    AutoreRepository autoreRepository;

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

}
