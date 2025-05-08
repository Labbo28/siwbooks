package it.uniroma3.siw.siwbooks.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.repository.LibroRepository;


@Service
public class LibroService {

    @Autowired
    LibroRepository libroRepository;

    public void getAllLibri(){
        libroRepository.findAll();
    }



}
