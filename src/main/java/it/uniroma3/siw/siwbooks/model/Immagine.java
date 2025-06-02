// Immagine.java
package it.uniroma3.siw.siwbooks.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Immagine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nome;
    private String path;
    
    // **Non** esistono più riferimenti a Libro o Autore qui:
    // rimuoviamo @ManyToOne verso Libro e @OneToOne verso Autore

    public Immagine(String nome, String path) {
        this.nome = nome;
        this.path = path;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Immagine immagine = (Immagine) o;
        return Objects.equals(id, immagine.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
