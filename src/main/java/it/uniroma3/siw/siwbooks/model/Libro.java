package it.uniroma3.siw.siwbooks.model;


import jakarta.persistence.GeneratedValue;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Libro {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
  private Long id;
  @NotBlank
  private String titolo;
  @NotBlank
  private int annoPubblicazione;
  
  @OneToMany(mappedBy = "libro")
  private List<Immagine> copertina;
   
  @ManyToMany
  private Set<Autore> autori;
  
  @OneToMany(mappedBy = "libro")
  private Set<Recensione> recensioni;
}
