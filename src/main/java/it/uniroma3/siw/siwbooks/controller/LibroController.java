package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.siwbooks.service.LibroService;



@Controller
public class LibroController {
    @Autowired
     private LibroService libroService;

     @GetMapping("/libri")
     public String getAllLibri() {
         libroService.getAllLibri();
         return "libri.html";
     }
     

}
