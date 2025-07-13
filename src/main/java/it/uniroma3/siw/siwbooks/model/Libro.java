// Libro.java
package it.uniroma3.siw.siwbooks.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank
    private String titolo;
    
    private int annoPubblicazione;
    
    // OneToMany unidirezionale: la FK (libro_id) verrà messa in tabella "immagine"
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "libro_id") 
    private List<Immagine> copertina = new ArrayList<>();
    
    @JsonIgnore
    @ManyToMany
    // (se non hai già definito la JoinTable in Autore, altrimenti specifica @JoinTable qui)
    private Set<Autore> autori = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "libro", fetch = FetchType.EAGER)
    private Set<Recensione> recensioni = new HashSet<>();
    
    public Libro(String titolo, int annoPubblicazione) {
        this.titolo = titolo;
        this.annoPubblicazione = annoPubblicazione;
    }
    
    public Libro(String titolo, int annoPubblicazione, Set<Autore> autori) {
        this.titolo = titolo;
        this.annoPubblicazione = annoPubblicazione;
        this.autori = autori;
    }
    
    public Libro(String titolo, int annoPubblicazione, Set<Autore> autori, List<Immagine> copertina) {
        this.titolo = titolo;
        this.annoPubblicazione = annoPubblicazione;
        this.autori = autori;
        this.copertina = copertina;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(id, libro.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
