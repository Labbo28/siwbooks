package it.uniroma3.siw.siwbooks.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NuovoAutoreDTO {
    private String nome;
    private String cognome;
    private String nazionalita;
    private String dataNascita; 
    private String dataMorte;    //può essere null se l'autore è ancora in vita
    private MultipartFile fileImmagine; // Nome dell'immagine associata all'autore

}
