package it.uniroma3.siw.siwbooks.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.siwbooks.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.siwbooks.model.Libro;
import java.util.List;

@RestController
@RequestMapping("/api/libri")
public class LibroRestController {

    @Autowired
    private LibroService libroService;

    @GetMapping("/search")
    public List<Libro> searchLibri(@RequestParam("query") String query) {
        return libroService.cercaLibri(query);
    }
}

