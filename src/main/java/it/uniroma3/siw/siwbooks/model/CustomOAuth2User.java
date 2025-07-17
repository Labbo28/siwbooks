package it.uniroma3.siw.siwbooks.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User, UserDetails {
    private OAuth2User oauth2User;
    private Utente utente;

    public CustomOAuth2User(OAuth2User oauth2User, Utente utente) {
        this.oauth2User = oauth2User;
        this.utente = utente;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = "ROLE_" + utente.getRuolo().name();
        return Collections.singleton(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getName() {
        return utente.getEmail();
    }

    // Metodi UserDetails
    @Override
    public String getUsername() {
        return utente.getEmail();
    }

    @Override
    public String getPassword() {
        return utente.getPassword();
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
    public boolean isEnabled() {
        return true;
    }

    // Getter per accedere all'utente
    public Utente getUtente() {
        return utente;
    }
}