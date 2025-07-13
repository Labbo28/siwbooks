package it.uniroma3.siw.siwbooks.controller;

import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.service.AutoreService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/autori")
public class AutoreRestController {

    @Autowired
    private AutoreService autoreService;

    @GetMapping("/search")
    public List<Autore> searchAutori(@RequestParam("query") String query) {
        return autoreService.cercaAutori(query);
    }
}
