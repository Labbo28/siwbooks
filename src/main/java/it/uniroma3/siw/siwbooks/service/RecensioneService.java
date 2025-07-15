package it.uniroma3.siw.siwbooks.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.dto.NuovaRecensioneDTO;
import it.uniroma3.siw.siwbooks.exceptions.AlreadyReviewedException;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.model.Recensione;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.repository.RecensioneRepository;
import jakarta.transaction.Transactional;
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
    public void creaRecensione(Long libroId, NuovaRecensioneDTO nuovaRecensioneDTO) 
    throws AlreadyReviewedException {
        Libro libro = libroService.getLibroById(libroId);
        Utente u= utenteService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(isAlreadyReviewed(libro, u)) {
            throw new AlreadyReviewedException(libroId);
        }
        else{
        // Crea una nuova recensione e imposta i campi dal DTO
        Recensione recensione = new Recensione();
        recensione.setTitolo(nuovaRecensioneDTO.getTitolo());
        recensione.setTesto(nuovaRecensioneDTO.getTesto());
        recensione.setVoto(nuovaRecensioneDTO.getVoto());
        // Imposta il libro associato alla recensione
        
        recensione.setLibro(libro);
        recensione.setUtente(u);
        u.getRecensioni().add(recensione);
         
        // Salva la recensione nel repository
        recensioneRepository.save(recensione);
    }
}

    public boolean isAlreadyReviewed (Libro libro, Utente utente) {
        return recensioneRepository.existsByLibroAndUtente(libro, utente);
    }

    public Recensione getRecensioneByLibroAndUtente(Libro libro, Utente utente) {
        return recensioneRepository.findByLibroAndUtente(libro, utente).orElse(null);
    }

    public void eliminaRecensione(Long libroId) {
       Libro libro = libroService.getLibroById(libroId);
       Utente currentUser = utenteService.getCurrentUser();
       Recensione recensione = recensioneRepository.findByLibroAndUtente(libro, currentUser).orElseThrow();
       recensioneRepository.delete(recensione);

               
    }

   public void modificaRecensione(Long libroId, NuovaRecensioneDTO modificaRecensioneDTO) {
    Utente currentUser = utenteService.getCurrentUser();
    Libro libro = libroService.getLibroById(libroId);
    
    Recensione esistente = getRecensioneByLibroAndUtente(libro, currentUser);
    if (esistente != null) {
        esistente.setVoto(modificaRecensioneDTO.getVoto());
        esistente.setTitolo(modificaRecensioneDTO.getTitolo());
        esistente.setTesto(modificaRecensioneDTO.getTesto());
        
        recensioneRepository.save(esistente);
    }
}

}
