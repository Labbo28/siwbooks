package it.uniroma3.siw.siwbooks.model;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Entity
@Data
public class Recensione {
   @Id
   @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
   private Long id;
   @Nullable
   private String titolo;
   @Lob
   @Nullable
   private String testo;
    @Min(1)
    @Max(5)
    @NotNull
    private int valutazione;
     
    @ManyToOne
    private Utente utente;

    @ManyToOne
    private Libro libro;
}
