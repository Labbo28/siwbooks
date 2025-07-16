package it.uniroma3.siw.siwbooks.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwbooks.model.Immagine;

public interface ImmagineRepository extends CrudRepository<Immagine, Long> {

    List<Immagine> findByUnlinkedTrue();

}
