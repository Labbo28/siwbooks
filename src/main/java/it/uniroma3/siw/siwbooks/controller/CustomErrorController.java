

package it.uniroma3.siw.siwbooks.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Ottieni il codice di stato dell'errore
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            // Gestisci diversi codici di errore con template specifici
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // Aggiungi eventuali attributi al modello
                model.addAttribute("errorMessage", "La pagina richiesta non è stata trovata");
                return "error/error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error-500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/error-403";
            }
            else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error/error-400";
            }
            else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "error/error-401";
            }
        }
        
        // Per tutti gli altri errori
        return "error";
    }
}
