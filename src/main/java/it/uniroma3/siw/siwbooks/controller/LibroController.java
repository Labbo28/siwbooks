package it.uniroma3.siw.siwbooks.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.siwbooks.dto.NuovaRecensioneDTO;
import it.uniroma3.siw.siwbooks.dto.NuovoLibroDTO;
import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.model.Recensione;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.service.AutoreService;
import it.uniroma3.siw.siwbooks.service.LibroService;
import it.uniroma3.siw.siwbooks.service.RecensioneService;
import it.uniroma3.siw.siwbooks.service.UtenteService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LibroController {

    private final AutoreService autoreService;
    @Autowired
    private RecensioneService recensioneService;
    @Autowired
     private LibroService libroService;
     @Autowired
        private UtenteService utenteService;

    LibroController(AutoreService autoreService) {
        this.autoreService = autoreService;
    }
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

@PostMapping("/libri/{id}/delete")
public String deleteLibro(Model model, @PathVariable("id")Long id ) {
    libroService.deleteLibro(id);
    model.addAttribute("successMessage", "Libro eliminato con successo");
   return "redirect:/libri";
}

@GetMapping("/libri/{id}/edit")
public String getFormModificaLibro(Model model, @PathVariable("id") Long id) {
    Libro libro = libroService.getLibroById(id);
    
    // Ottieni tutti gli autori disponibili
    List<Autore> tuttiAutori = autoreService.getAllAutori();
    
    // Mostra tutti gli autori (più user-friendly)
    List<Autore> autoriDisponibili = tuttiAutori;
    
    // Crea il DTO precompilato con i dati del libro
    NuovoLibroDTO nuovoLibroDTO = new NuovoLibroDTO();
    nuovoLibroDTO.setTitolo(libro.getTitolo());
    nuovoLibroDTO.setAnnoPubblicazione(libro.getAnnoPubblicazione());
    
    // Precompila gli ID degli autori attualmente associati
    List<Long> autoriAttualiIds = libro.getAutori().stream()
        .map(Autore::getId)
        .collect(Collectors.toList());
    nuovoLibroDTO.setIdAutore(autoriAttualiIds);
    
    // Aggiungi tutto al model
    model.addAttribute("libro", libro);
    model.addAttribute("nuovoLibroDTO", nuovoLibroDTO);
    model.addAttribute("autoriDisponibili", autoriDisponibili);
    model.addAttribute("autoriAttuali", libro.getAutori());
    
    return "modifica-libro";
}



     @PostMapping("/libri/{id}/edit")
public String modificaLibro(@PathVariable("id") Long id, 
                           @Valid @ModelAttribute NuovoLibroDTO nuovoLibroDTO,
                           BindingResult bindingResult, 
                           Model model) {
    
    if (bindingResult.hasErrors()) {
        // Ricarica i dati necessari per il form in caso di errori
        Libro libro = libroService.getLibroById(id);
        List<Autore> autoriDisponibili = autoreService.getAllAutori();
        
        model.addAttribute("libro", libro);
        model.addAttribute("autoriDisponibili", autoriDisponibili);
        model.addAttribute("autoriAttuali", libro.getAutori());
        
        return "modifica-libro";
    }
    
    try {
        libroService.modificaLibro(id, nuovoLibroDTO);
        return "redirect:/libri/" + id;
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Errore durante la modifica del libro: " + e.getMessage());
        
        // Ricarica i dati per il form
        Libro libro = libroService.getLibroById(id);
        List<Autore> autoriDisponibili = autoreService.getAllAutori();
        
        model.addAttribute("libro", libro);
        model.addAttribute("autoriDisponibili", autoriDisponibili);
        model.addAttribute("autoriAttuali", libro.getAutori());
        
        return "modifica-libro";
    }
}

}
