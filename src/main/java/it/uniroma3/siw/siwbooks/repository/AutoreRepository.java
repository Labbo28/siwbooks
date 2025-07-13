package it.uniroma3.siw.siwbooks.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;


import it.uniroma3.siw.siwbooks.model.Autore;


public interface AutoreRepository extends CrudRepository<Autore, Long> {

    Collection<Autore> findByNomeContainingIgnoreCase(String term, PageRequest of);

    List<Autore> findByNomeIgnoreCaseContainingOrCognomeIgnoreCaseContaining(String query, String query2);

}
