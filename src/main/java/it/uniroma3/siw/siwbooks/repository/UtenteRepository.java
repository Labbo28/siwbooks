package it.uniroma3.siw.siwbooks.repository;

import java.util.Optional;


import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwbooks.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
    Utente findByEmail(String email);
    Utente findByEmailAndProvider(String email, String provider);
    Optional<Utente> findByProviderAndProviderId(String provider, String providerId);
    boolean existsByEmail(String username);
}
