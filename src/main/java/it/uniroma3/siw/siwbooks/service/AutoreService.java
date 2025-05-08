package it.uniroma3.siw.siwbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.repository.AutoreRepository;


@Service
public class AutoreService {

    @Autowired
    AutoreRepository autoreRepository;

    public void getAllAutori(){
        autoreRepository.findAll();
    }

}
