package it.uniroma3.siw.siwbooks.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwbooks.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long> {

    List<Libro> findByTitoloIgnoreCaseContaining(String query);
}
