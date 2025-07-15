package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.siwbooks.dto.NuovaRecensioneDTO;
import it.uniroma3.siw.siwbooks.exceptions.AlreadyReviewedException;
import it.uniroma3.siw.siwbooks.service.RecensioneService;
import org.springframework.web.bind.annotation.RequestBody;


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

    @PostMapping("/libri/{id}/recensioni/modifica")
public String modificaRecensione(@PathVariable("id") Long libroId, 
                                @ModelAttribute("modificaRecensioneDTO") NuovaRecensioneDTO modificaRecensioneDTO,
                                Model model) {
    try {
        recensioneService.modificaRecensione(libroId, modificaRecensioneDTO);
        return "redirect:/libri/" + libroId;
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Errore durante la modifica della recensione.");
        return "redirect:/libri/" + libroId;
    }
}
    
@PostMapping("/libri/{id}/recensioni/elimina")
public String eliminaRecensione( @PathVariable("id") Long libroId) {
    recensioneService.eliminaRecensione(libroId);
    return "redirect:/libri/" + libroId;
}


}
