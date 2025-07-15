package it.uniroma3.siw.siwbooks.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.siwbooks.dto.NuovaImmagineDTO;
import it.uniroma3.siw.siwbooks.dto.NuovoAutoreDTO;
import it.uniroma3.siw.siwbooks.dto.NuovoLibroDTO;
import it.uniroma3.siw.siwbooks.dto.TipoImmagine;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.service.AutoreService;
import it.uniroma3.siw.siwbooks.service.ImmagineService;
import it.uniroma3.siw.siwbooks.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;







@Controller
public class AdminController {

    @Autowired
    private ImmagineService immagineService;
    @Autowired 
    LibroService libroService;
    @Autowired 
    AutoreService autoreService;

    @GetMapping("/admin")
    public String getAdminPanel() {
        return "adminPanel";
    }
    

    @GetMapping("/libri/new")
    public String getNewBookForm(Model model) {
        model.addAttribute("nuovoLibro", new NuovoLibroDTO());
        return "nuovoLibro";
    }

    @PostMapping("/libri/new")
    public String addNewBook(@Valid NuovoLibroDTO nuovoLibroDTO, BindingResult bindingResult , Model model) {
        
        if (bindingResult.hasErrors()){
            return "nuovoLibro";
        }

        List<Immagine> immaginiCopertina = new ArrayList<Immagine>();
        for (MultipartFile file : nuovoLibroDTO.getFileImmagine()) 
        {
            NuovaImmagineDTO nuovaImmagineDTO = new NuovaImmagineDTO(file,TipoImmagine.COPERTINA);
            immaginiCopertina.add(immagineService.uploadImmagine(nuovaImmagineDTO));

        }
        libroService.salvaNuovoLibro(nuovoLibroDTO,immaginiCopertina);


        return "redirect:/libri?upload=success";
    }
    

    @GetMapping("/autori/new")
    public String getNewAuthorForm(Model model) {
        model.addAttribute("nuovoAutore", new NuovoAutoreDTO());
        return "nuovoAutore";
    }

    @PostMapping("/autori/new")
    public String postMethodName(@Valid NuovoAutoreDTO nuovoAutoreDTO, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()){
            return "nuovoAutore";
        }
       
        NuovaImmagineDTO nuovaImmagineDTO= new NuovaImmagineDTO(nuovoAutoreDTO.getFileImmagine(),TipoImmagine.AUTORE);
        Immagine immagine = immagineService.uploadImmagine(nuovaImmagineDTO);

        autoreService.salvaNuovoAutore(nuovoAutoreDTO,immagine);

        return "redirect:/autori?upload=success";
    }
    


    /* 
    @GetMapping("/immagini/new")
    public String getNewImageForm(Model model) {
        // Metto in model un oggetto DTO appena creato con nome “nuovaImmagine”
        model.addAttribute("nuovaImmagine", new NuovaImmagineDTO());
        return "nuovaImmagine";
    }

    // Process the submission
    @PostMapping("/immagini/new")
    public String uploadImage(
            @ModelAttribute("nuovaImmagine") @Valid NuovaImmagineDTO dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            // Se ci sono errori di validazione, ricarico il form
            return "nuovaImmagine";
        }

        try {
            immagineService.uploadImmagine(dto);
            model.addAttribute("successMessage", "Immagine caricata con successo!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Qualcosa è andato storto durante l’upload.");
            return "nuovaImmagine";
        }

        // Dopo il redirect, ricarica la pagina vuota (o vai da un’altra parte)
        return "redirect:/immagini/new?upload=success";
    }
    
    */
    
    
}
