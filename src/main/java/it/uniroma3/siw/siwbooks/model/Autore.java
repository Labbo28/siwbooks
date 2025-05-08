package it.uniroma3.siw.siwbooks.model;

import java.util.Set;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Autore {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String cognome;
    @NotBlank
    private String nazionalita;
    @NotBlank
    private String dataNascita;
    @Nullable
    private String dataMorte;
    
    @ManyToMany(mappedBy = "autori")
    private Set<Libro> libri;

    @OneToOne
    private Immagine immagine;
}
