package it.uniroma3.siw.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;

    public void registraUtente(String nome, String cognome, String email, String password ,Ruolo ruolo) {
       
        // Controlla se l'email è già registrata
        //da implementare
        if (utenteRepository.existsByEmail(email)) {
            throw new alreadyRegisteredException("Email già registrata");
        }
        else{
        Utente nuovoUtente = new Utente(nome, cognome, email, password, ruolo);
        utenteRepository.save(nuovoUtente);
        }
    }

}
