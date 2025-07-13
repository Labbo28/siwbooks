// Autore.java
package it.uniroma3.siw.siwbooks.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Autore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank
    private String nome;
    
    @NotBlank
    private String cognome;
    
    @NotBlank
    private String nazionalita;
    
    @NotBlank
    private String dataNascita;
    
    private String dataMorte;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "autori")
    private Set<Libro> libri = new HashSet<>();
    
    // Un solo lato OneToOne → Autore è owner, con join column in tabella "autore"
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "immagine_id")
    private Immagine immagine;
    
    public Autore(String nome, String cognome, String nazionalita, String dataNascita, String dataMorte) {
        this.nome = nome;
        this.cognome = cognome;
        this.nazionalita = nazionalita;
        this.dataNascita = dataNascita;
        this.dataMorte = dataMorte;
    }

    public Autore(String nome, String cognome, String nazionalita, String dataNascita, String dataMorte,Immagine immagine) {
        this.nome = nome;
        this.cognome = cognome;
        this.nazionalita = nazionalita;
        this.dataNascita = dataNascita;
        this.dataMorte = dataMorte;
        this.immagine=immagine;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autore autore = (Autore) o;
        return Objects.equals(id, autore.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
