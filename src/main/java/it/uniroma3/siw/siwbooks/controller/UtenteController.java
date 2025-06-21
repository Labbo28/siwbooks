package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.siwbooks.dto.UserRegistrationDTO;
import it.uniroma3.siw.siwbooks.dto.UserLoginDTO;
import it.uniroma3.siw.siwbooks.exceptions.alreadyRegisteredException;
import it.uniroma3.siw.siwbooks.model.UserPrincipal;
import it.uniroma3.siw.siwbooks.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/")
    public String homePage(Authentication authentication) {
        // Debug per verificare l'autenticazione
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("=== DEBUG AUTENTICAZIONE ===");
            System.out.println("Utente autenticato: " + authentication.getName());
            System.out.println("Principal type: " + authentication.getPrincipal().getClass().getName());
            
            // Mostra tutti i ruoli/authorities
            System.out.println("Authorities:");
            authentication.getAuthorities().forEach(auth -> 
                System.out.println("  - " + auth.getAuthority())
            );
            
            // Cast per accedere ai dati dell'utente
            if (authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                System.out.println("Nome utente: " + userPrincipal.getUtente().getNome());
                System.out.println("Email utente: " + userPrincipal.getUtente().getEmail());
                System.out.println("Ruolo utente: " + userPrincipal.getUtente().getRuolo());
            }
            System.out.println("============================");
        } else {
            System.out.println("Nessun utente autenticato");
        }
        return "index";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("userRegistrationDTO")) {
            model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        }
        return "register.html";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRegistrationDTO") UserRegistrationDTO userRegistrationDTO,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword",
                    "Le password non corrispondono");
        }
        
        if (bindingResult.hasErrors()) {
            return "register.html";
        }
        
        try {
            utenteService.registraUtente(userRegistrationDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registrazione completata con successo! Ora puoi effettuare il login.");
            return "redirect:/login";
        } catch (alreadyRegisteredException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "register.html";
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "Si è verificato un errore durante la registrazione. Riprova più tardi.");
            return "register.html";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("userLoginDTO")) {
            model.addAttribute("userLoginDTO", new UserLoginDTO());
        }
        return "login.html";
    }

    
    
}