package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import it.uniroma3.siw.siwbooks.dto.AutoreDTO;
import it.uniroma3.siw.siwbooks.dto.NuovaImmagineDTO;
import it.uniroma3.siw.siwbooks.dto.NuovoAutoreDTO;
import it.uniroma3.siw.siwbooks.dto.TipoImmagine;
import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.service.AutoreService;
import it.uniroma3.siw.siwbooks.service.ImmagineService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;






@Controller
public class AutoreController {
    @Autowired
    AutoreService autoreService;
    @Autowired
    ImmagineService immagineService;
    @GetMapping("/autori")
    public String getAllAutori(Model model) {
        model.addAttribute("autori", autoreService.getAllAutori());
        return "autori.html";
    }

    @GetMapping("/autori/{id}")
    public String getMethodName(@PathVariable("id") Long id, Model model) {
        Autore autore = autoreService.getAutoreById(id);
        model.addAttribute("autore", autore);
        return "autore.html";
    }

    @GetMapping("/autori/{id}/edit") 
    public String modificaAutore(@PathVariable("id") Long id, Model model) {
        Autore autore = autoreService.getAutoreById(id);
        if (autore == null) {
            return "error.html"; // Gestione dell'errore se l'autore non esiste
        }
        NuovoAutoreDTO nuovoAutoreDTO = new NuovoAutoreDTO();
        nuovoAutoreDTO.setNome(autore.getNome());
        nuovoAutoreDTO.setCognome(autore.getCognome());
        nuovoAutoreDTO.setNazionalita(autore.getNazionalita());
        nuovoAutoreDTO.setDataNascita(autore.getDataNascita());
        nuovoAutoreDTO.setDataMorte(autore.getDataMorte());
        model.addAttribute("nuovoAutoreDTO", nuovoAutoreDTO);
        model.addAttribute("autore", autore);
        return "modifica-autore.html"; // Pagina per modificare i dettagli dell'autore

    }

    @PostMapping("/autori/{id}/edit")
    public String modificaAutore(@ModelAttribute("nuovoAutoreDTO") NuovoAutoreDTO nuovoAutoreDTO,
     Model model, @PathVariable("id") Long id) {
        Autore autore = autoreService.getAutoreById(id);
        if (hasValidFile(nuovoAutoreDTO.getFileImmagine())) {
            NuovaImmagineDTO img = new NuovaImmagineDTO(nuovoAutoreDTO.getFileImmagine(), TipoImmagine.AUTORE);
            Immagine immagine = immagineService.uploadImmagine(img);
            autore.getImmagine().setUnlinked(true);  // variabile da usare durante il cleanup delle immagini
            
            autoreService.modifcaAutore(autore, nuovoAutoreDTO, immagine);
        } else {
            autoreService.modifcaAutore(autore, nuovoAutoreDTO, null);
        }
       return "redirect:/autori/" + id; // Reindirizza alla pagina dell'autore dopo la modifica
    }
    @PostMapping("/autori/{id}/remove-data-morte")
    public String removeDataMorte(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        System.out.println("=== INIZIO RIMOZIONE DATA MORTE ===");
        System.out.println("ID autore: " + id);
        
        try {
            Autore autore = autoreService.getAutoreById(id);
            System.out.println("Autore trovato: " + autore.getNome() + " " + autore.getCognome());
            System.out.println("Data morte prima della rimozione: " + autore.getDataMorte());
            
            autoreService.rimuoviDataMorte(autore);
            
            System.out.println("Servizio rimuoviDataMorte chiamato");
            
            // Verifica che sia stata rimossa ricaricando dall'DB
            Autore autoreAggiornato = autoreService.getAutoreById(id);
            System.out.println("Data morte dopo la rimozione: " + autoreAggiornato.getDataMorte());
            
            System.out.println("=== FINE RIMOZIONE DATA MORTE ===");
            
            return "redirect:/autori/" + id + "/edit"; // Cambia questo URL se necessario
            
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/autori/" + id + "/edit";
        }
    }

    @GetMapping("/autori/{id}/{libroId}/delete")
    public String deleteLibroDaAutore(
            @PathVariable("id") Long autoreId,
            @PathVariable("libroId") Long libroId
    ) {
        // 1. Rimuovi il libro dall'autore
        autoreService.rimuoviLibroDaAutore(autoreId, libroId);
        System.out.println("Libro rimosso dall'autore con ID: " + autoreId + " e libro ID: " + libroId);

        // 2. Reindirizza alla pagina di dettaglio dell'autore
        return "redirect:/autori/" + autoreId;
    }
    
    private boolean hasValidFile(MultipartFile file) {
    return file != null && !file.isEmpty() && file.getSize() > 0;
}
}
