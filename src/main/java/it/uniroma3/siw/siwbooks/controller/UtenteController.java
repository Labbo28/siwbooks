package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
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
import it.uniroma3.siw.siwbooks.exceptions.UsernameAlreadyExistsException;
import it.uniroma3.siw.siwbooks.exceptions.alreadyRegisteredException;
import it.uniroma3.siw.siwbooks.model.CustomOAuth2User;
import it.uniroma3.siw.siwbooks.model.UserPrincipal;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.service.UtenteService;
import jakarta.validation.Valid;


@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

   @GetMapping("/")
public String homePage(Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
        System.out.println("=== DEBUG AUTENTICAZIONE ===");
        System.out.println("Utente autenticato: " + authentication.getName());
        System.out.println("Principal type: " + authentication.getPrincipal().getClass().getName());
        
        authentication.getAuthorities().forEach(auth -> 
            System.out.println("  - " + auth.getAuthority())
        );
        
        // Gestisce sia UserPrincipal che CustomOAuth2User
        Utente utente = null;
        if (authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            utente = userPrincipal.getUtente();
        } else if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
            utente = oauth2User.getUtente();
        }
        
        if (utente != null) {
            System.out.println("Nome utente: " + utente.getNome());
            System.out.println("Email utente: " + utente.getEmail());
            System.out.println("Ruolo utente: " + utente.getRuolo());
            System.out.println("Provider: " + utente.getProvider());
        }
        System.out.println("============================");
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
            
        } catch (alreadyRegisteredException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "register.html";}
            catch(UsernameAlreadyExistsException e) {
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "Si è verificato un errore durante la registrazione. Riprova più tardi.");
            return "register.html";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("userLoginDTO")) {
            model.addAttribute("userLoginDTO", new UserLoginDTO());
        }
        return "login.html";
    }

    
    
}