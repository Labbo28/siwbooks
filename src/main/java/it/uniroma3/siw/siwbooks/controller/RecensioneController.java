package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.siwbooks.dto.NuovaRecensioneDTO;
import it.uniroma3.siw.siwbooks.exceptions.AlreadyReviewedException;
import it.uniroma3.siw.siwbooks.service.RecensioneService;

@Controller
public class RecensioneController {
    @Autowired
    private RecensioneService recensioneService;


    @PostMapping("/libri/{id}/recensioni")
    public String inviaRecensione(@PathVariable ("id") Long libroId, 
    NuovaRecensioneDTO nuovaRecensioneDTO, Model model) {
        try{
        recensioneService.creaRecensione(libroId, nuovaRecensioneDTO);
        }
        catch (AlreadyReviewedException e) {
            model.addAttribute("errorMessage", "Hai già recensito questo libro.");
            return "redirect:/libri/" + libroId; // Redirect to an error page or handle the error appropriately
        }
        return "redirect:/libri/" + libroId;
    }
    

}
