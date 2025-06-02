package it.uniroma3.siw.siwbooks.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.StreamSupport;

import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.repository.LibroRepository;


@Service
public class LibroService {

    @Autowired
    LibroRepository libroRepository;

    public List<Libro> getAllLibri(){
        return StreamSupport.stream(libroRepository.findAll().spliterator(), false)
                            .toList();
    }

    public Libro getLibroById(Long id) {
        return libroRepository.findById(id).orElse(null);
    }



}
