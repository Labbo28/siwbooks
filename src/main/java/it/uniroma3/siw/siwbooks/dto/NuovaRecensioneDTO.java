package it.uniroma3.siw.siwbooks.dto;

import lombok.Data;

@Data
public class NuovaRecensioneDTO {
    private int voto;
    private String titolo;
    private String testo;
}
