package it.uniroma3.siw.siwbooks.exceptions;


public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
