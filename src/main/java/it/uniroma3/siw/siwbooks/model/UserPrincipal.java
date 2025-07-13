package it.uniroma3.siw.siwbooks.model;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    private Utente utente;

    public UserPrincipal(Utente utente) {
        this.utente = utente;
    }

    // Aggiungi questo getter pubblico per esporre l'oggetto Utente
    public Utente getUtente() {
        return utente;
    }

    @Override
    public String getUsername() {
        return utente.getEmail();
    
    }

    @Override
    public String getPassword() {
        return utente.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security richiede il prefisso ROLE_ per i ruoli
        String roleName = "ROLE_" + utente.getRuolo().name();
        System.out.println("Ruolo assegnato: " + roleName); // Debug
        return Collections.singleton(new SimpleGrantedAuthority(roleName));
    }
}