package it.uniroma3.siw.siwbooks.exceptions;
public class alreadyRegisteredException extends RuntimeException {
    
    private final String email;
    
    public alreadyRegisteredException(String email) {
        super("L'utente con email " + email + " è già registrato");
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}