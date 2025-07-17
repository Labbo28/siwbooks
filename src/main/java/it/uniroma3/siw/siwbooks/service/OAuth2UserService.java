package it.uniroma3.siw.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.model.CustomOAuth2User;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        return processOAuth2User(userRequest, oauth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String nome = oauth2User.getAttribute("given_name");
        String cognome = oauth2User.getAttribute("family_name");
        String providerId = oauth2User.getAttribute("sub");
        String imageUrl = oauth2User.getAttribute("picture");
        
        Utente utente = utenteRepository.findByEmail(email);
        
        if (utente == null) {
            // Crea nuovo utente
            utente = new Utente(nome, cognome, email, "GOOGLE", providerId, imageUrl);
            utenteRepository.save(utente);
        } else if (!"GOOGLE".equals(utente.getProvider())) {
            // Utente già esistente ma con provider diverso
            throw new OAuth2AuthenticationException("Email già registrata con un altro provider");
        }
        
        return new CustomOAuth2User(oauth2User, utente);
    }
}