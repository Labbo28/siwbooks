package it.uniroma3.siw.siwbooks.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.model.UserPrincipal;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Utente utente = utenteRepository.findByEmail(email);
		// Implement the logic to load user details by username
        if(utente == null){
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
        else {
            System.out.println("User found: " + utente.getEmail());
            return new UserPrincipal(utente);
        }
        }
		
	}

