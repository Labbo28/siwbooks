package it.uniroma3.siw.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;
import it.uniroma3.siw.siwbooks.dto.UserRegistrationDTO;
import it.uniroma3.siw.siwbooks.exceptions.alreadyRegisteredException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;
    @Autowired
    PasswordEncoder passwordEncoder;   

    public void registraUtente(UserRegistrationDTO userRegistrationDTO) throws alreadyRegisteredException {
       
        // Controlla se l'email è già registrata
        if (utenteRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new alreadyRegisteredException(userRegistrationDTO.getEmail());
        }
        else{
            String passwordCifrata = passwordEncoder.encode(userRegistrationDTO.getPassword());
        Utente nuovoUtente = new Utente
        (userRegistrationDTO.getNome(),
         userRegistrationDTO.getCognome(),
          userRegistrationDTO.getEmail(),
           passwordCifrata, 
           Ruolo.USER);
        utenteRepository.save(nuovoUtente);
        }
    }

}
