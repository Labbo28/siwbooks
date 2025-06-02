package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.service.AutoreService;



@Controller
public class AutoreController {
    @Autowired
    AutoreService autoreService;
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
    
    
}
