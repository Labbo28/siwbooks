package it.uniroma3.siw.siwbooks.exceptions;

public class AlreadyReviewedException extends RuntimeException {
    
public AlreadyReviewedException(Long libroId){
super("Il libro con ID " + libroId + " è già stato recensito da questo utente.");
}
}
