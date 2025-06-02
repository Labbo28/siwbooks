package it.uniroma3.siw.siwbooks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.service.LibroService;



@Controller
public class LibroController {
    @Autowired
     private LibroService libroService;

     @GetMapping("/libri")
     public String getAllLibri(Model model) {
         // Aggiungi i libri al modello per poterli visualizzare nella vista
         model.addAttribute("libri", libroService.getAllLibri());
         
         // Debug: stampa i libri nella console
         System.out.println("=== DEBUG LIBRI ===");
         List<Libro> libri = libroService.getAllLibri();
         libri.forEach(libro -> System.out.println(libro.getTitolo()));
         System.out.println("====================");
         
         // Restituisce il nome della vista da renderizzare
        
         return "libri.html";
     }

     @GetMapping("/libri/{id}")
        public String getLibroById(Model model, @PathVariable("id") Long id) {
            // Recupera il libro per ID e aggiungilo al modello
            Libro libro = libroService.getLibroById(id);
            model.addAttribute("libro", libro);
            return "libro.html"; // Vista per visualizzare i dettagli del libro
            
        }
     

}
