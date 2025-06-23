package it.uniroma3.siw.siwbooks.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsernameAlreadyExistsException(String username) {
        super("Username già esistente: " + username);
    }

}
