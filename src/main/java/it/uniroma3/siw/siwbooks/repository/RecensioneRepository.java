package it.uniroma3.siw.siwbooks.repository;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.model.Recensione;
import it.uniroma3.siw.siwbooks.model.Utente;


public interface RecensioneRepository extends CrudRepository<Recensione, Long> {

    boolean existsByLibroAndUtente(Libro libro, Utente utente);
   

}
