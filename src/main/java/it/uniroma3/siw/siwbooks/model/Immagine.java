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

    public Immagine(String nome, String path) {
       
        this.path = path;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;   
    private String path;
    private boolean unlinked = false; // Indica se l'immagine è stata scollegata da un autore o libro
    

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
