package it.uniroma3.siw.siwbooks.model;

import jakarta.persistence.GeneratedValue;

import java.util.Set;

import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Utente {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Ruolo ruolo;
   
    @OneToMany(mappedBy = "utente")
    private Set <Recensione> recensioni;

    public Utente(String nome, String cognome, String email, String password, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

}
