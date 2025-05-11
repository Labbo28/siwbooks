package it.uniroma3.siw.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;
import it.uniroma3.siw.siwbooks.dto.UserRegistrationDTO;
import it.uniroma3.siw.siwbooks.dto.UserLoginDTO;
import it.uniroma3.siw.siwbooks.exceptions.EmailNotFoundException;
import it.uniroma3.siw.siwbooks.exceptions.InvalidPasswordException;
import it.uniroma3.siw.siwbooks.exceptions.alreadyRegisteredException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    // Rimettere la password cifrata
    public void registraUtente(UserRegistrationDTO userRegistrationDTO) throws alreadyRegisteredException {
        // Controlla se l'email è già registrata
        if (utenteRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new alreadyRegisteredException(userRegistrationDTO.getEmail());
        } else {
            // String passwordCifrata = passwordEncoder.encode(userRegistrationDTO.getPassword());
            Utente nuovoUtente = new Utente(
                userRegistrationDTO.getNome(),
                userRegistrationDTO.getCognome(),
                userRegistrationDTO.getEmail(),
                // passwordCifrata
                userRegistrationDTO.getPassword(),
                Ruolo.USER
            );
            utenteRepository.save(nuovoUtente);
        }
    }

    // Rimettere la password cifrata
    public Utente loginUtente(UserLoginDTO userLoginDTO) {
        // 1. Controlla che l'email esista
        if (!utenteRepository.existsByEmail(userLoginDTO.getEmail())) {
            throw new EmailNotFoundException("Email non registrata: " + userLoginDTO.getEmail());
        }
        
        // 2. Recupera l'utente
        Utente utente = utenteRepository.findByEmail(userLoginDTO.getEmail());
        
        /* 3. Confronta la password hashed
         * if (!passwordEncoder.matches(userLoginDTO.getPassword(), utente.getPassword())) {
         * throw new InvalidPasswordException("Password non corretta per l'email indicata.");
         * }
         */
        
        // 3. Confronta la password in chiaro
        if (!userLoginDTO.getPassword().equals(utente.getPassword())) {
            throw new InvalidPasswordException("Password non corretta per l'email indicata.");
        }
        
        // 4. Ritorna l'utente autenticato
        return utente;
    }
}