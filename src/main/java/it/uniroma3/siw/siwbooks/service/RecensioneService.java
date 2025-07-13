package it.uniroma3.siw.siwbooks.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.dto.NuovaRecensioneDTO;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.model.Recensione;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.repository.RecensioneRepository;
import jakarta.transaction.Transactional;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class RecensioneService {
 
    @Autowired
    RecensioneRepository recensioneRepository;
    @Autowired
    LibroService libroService;

    @Autowired UtenteService utenteService;

    @Transactional
    public void creaRecensione(Long libroId, NuovaRecensioneDTO nuovaRecensioneDTO) {
       
        // Crea una nuova recensione e imposta i campi dal DTO
        Recensione recensione = new Recensione();
        recensione.setTitolo(nuovaRecensioneDTO.getTitolo());
        recensione.setTesto(nuovaRecensioneDTO.getTesto());
        recensione.setVoto(nuovaRecensioneDTO.getVoto());
        // Imposta il libro associato alla recensione
        Libro libro = libroService.getLibroById(libroId);
        recensione.setLibro(libro);
        Utente u= utenteService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        recensione.setUtente(u);
        u.getRecensioni().add(recensione);
         
        // Salva la recensione nel repository
        recensioneRepository.save(recensione);
    }

}
