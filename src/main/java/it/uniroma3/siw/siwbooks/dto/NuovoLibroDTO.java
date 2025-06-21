package it.uniroma3.siw.siwbooks.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NuovoLibroDTO {
  private String titolo;
  private Integer annoPubblicazione;
  private List<Long> idAutore;
  private List<MultipartFile> fileImmagine;
  

}
