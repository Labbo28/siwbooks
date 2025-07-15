package it.uniroma3.siw.siwbooks.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.siwbooks.dto.NuovaRecensioneDTO;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.model.Recensione;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.service.LibroService;
import it.uniroma3.siw.siwbooks.service.RecensioneService;
import it.uniroma3.siw.siwbooks.service.UtenteService;



@Controller
public class LibroController {
    @Autowired
    private RecensioneService recensioneService;
    @Autowired
     private LibroService libroService;
     @Autowired
        private UtenteService utenteService;
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
    Libro libro = libroService.getLibroById(id);
    Utente current = utenteService.getCurrentUser();

    // La recensione dell’utente corrente (se esiste)
    Recensione myReview = recensioneService.getRecensioneByLibroAndUtente(libro, current);
    boolean canReview = (myReview == null);

    // Prepara lista recensioni: prima la mia (se c'è), poi le altre
    List<Recensione> others = libro.getRecensioni().stream()
        .filter(r -> !r.getUtente().equals(current))
        .toList();
    List<Recensione> all = new ArrayList<>();
    if (myReview != null) all.add(myReview);
    all.addAll(others);

    model.addAttribute("libro", libro);
    model.addAttribute("utente", current);
    model.addAttribute("myReview", myReview);
    model.addAttribute("canReview", canReview);
    model.addAttribute("recensioni", all);
    model.addAttribute("nuovaRecensioneDTO", new NuovaRecensioneDTO());
    model.addAttribute("modificaRecensioneDTO", new NuovaRecensioneDTO());  // per il form di modifica

    return "libro";
}

     

}
