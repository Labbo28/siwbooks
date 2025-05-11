package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import it.uniroma3.siw.siwbooks.service.AutoreService;


@Controller
public class AutoreController {
    @Autowired
    AutoreService autoreService;
    @GetMapping("/autori")
    public String getAllAutori() {
        autoreService.getAllAutori();
        return "autori.html";
    }
    
}
