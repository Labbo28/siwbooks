package it.uniroma3.siw.siwbooks.model;

import jakarta.persistence.GeneratedValue;

import java.util.Set;

import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
public class Utente {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String password; // Nullable per utenti OAuth2
    private Ruolo ruolo;
    private String username;
    
    // Nuovi campi per OAuth2
    private String provider; // "LOCAL" o "GOOGLE"
    private String providerId; // Google User ID
    private String imageUrl; // URL immagine profilo Google
   
    @OneToMany(mappedBy = "utente")
    @EqualsAndHashCode.Exclude
    private Set<Recensione> recensioni;

    // Costruttore per utenti locali
    public Utente(String nome, String cognome, String email, String password, Ruolo ruolo, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
        this.username = username;
        this.provider = "LOCAL";
    }
    
    // Costruttore per utenti OAuth2
    public Utente(String nome, String cognome, String email, String provider, String providerId, String imageUrl) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = email;
        this.provider = provider;
        this.providerId = providerId;
        this.imageUrl = imageUrl;
        this.ruolo = Ruolo.USER; // Default per utenti OAuth2
    }
}