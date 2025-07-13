package it.uniroma3.siw.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.model.UserPrincipal;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;
import it.uniroma3.siw.siwbooks.dto.UserRegistrationDTO;
import it.uniroma3.siw.siwbooks.exceptions.UsernameAlreadyExistsException;
import it.uniroma3.siw.siwbooks.exceptions.alreadyRegisteredException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;

    
    
    @Autowired
    PasswordEncoder passwordEncoder;

    // Rimettere la password cifrata
    public Utente registraUtente(UserRegistrationDTO userRegistrationDTO) throws alreadyRegisteredException , 
    UsernameAlreadyExistsException {
        Utente nuovoUtente;
        if(utenteRepository.existsByEmail(userRegistrationDTO.getUsername())) {
            throw new alreadyRegisteredException(userRegistrationDTO.getUsername());
        }
        // Controlla se l'email è già registrata
        if (utenteRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new alreadyRegisteredException(userRegistrationDTO.getEmail());
        } else {
                nuovoUtente = new Utente(
                userRegistrationDTO.getNome(),
                userRegistrationDTO.getCognome(),
                userRegistrationDTO.getEmail(),
                passwordEncoder.encode(userRegistrationDTO.getPassword()),
                Ruolo.USER,
                userRegistrationDTO.getUsername()
            );
            utenteRepository.save(nuovoUtente);
        }
        return nuovoUtente;
    }

     public Utente GetCurrentUser() {
        Object principal = SecurityContextHolder
                               .getContext()
                               .getAuthentication()
                               .getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUtente();
        }
        return null; // o lancia eccezione se vuoi garantire auth
    }

     public Utente findByEmail(String name) {
        return utenteRepository.findByEmail(name);
                
     }

     public Utente getCurrentUser(){
        return findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

     }

    
}