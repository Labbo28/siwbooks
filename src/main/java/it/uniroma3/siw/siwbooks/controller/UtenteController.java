package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.siwbooks.dto.UserRegistrationDTO;
import it.uniroma3.siw.siwbooks.dto.UserLoginDTO;
import it.uniroma3.siw.siwbooks.exceptions.EmailNotFoundException;
import it.uniroma3.siw.siwbooks.exceptions.InvalidPasswordException;
import it.uniroma3.siw.siwbooks.exceptions.alreadyRegisteredException;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.service.UtenteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        // Aggiungi un oggetto vuoto al modello se non è già presente
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
        // Validazione personalizzata per confrontare le password
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

    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {
        if (bindingResult.hasErrors()) {
            // Validazione fallita (campo email o password vuoti o malformati)
            return "login.html";
        }
        try {
            // Login e ottenimento dell'utente
            Utente utente = utenteService.loginUtente(userLoginDTO);
            
            // Salvataggio dell'utente nella sessione
            session.setAttribute("utente", utente);
            
            redirectAttributes.addFlashAttribute("successMessage", "Login effettuato con successo!");
            return "redirect:/";
        } catch (EmailNotFoundException e) {
            model.addAttribute("errorEmail", e.getMessage());
            return "login.html";
        } catch (InvalidPasswordException e) {
            model.addAttribute("errorPassword", e.getMessage());
            return "login.html";
        }
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Rimuovi l'utente dalla sessione
        session.removeAttribute("utente");
        
        // Opzionale: invalidare completamente la sessione
        session.invalidate();
        
        redirectAttributes.addFlashAttribute("successMessage", "Logout effettuato con successo!");
        return "redirect:/";
    }
}