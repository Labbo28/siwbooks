package it.uniroma3.siw.siwbooks.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwbooks.repository.RecensioneRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RecensioneService {
 
    @Autowired
    RecensioneRepository recensioneRepository;

}
