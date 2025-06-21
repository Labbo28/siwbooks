package it.uniroma3.siw.siwbooks.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NuovaImmagineDTO {


    public NuovaImmagineDTO(MultipartFile multipartFile,TipoImmagine tipoImmagine){
        this.fileImmagine=multipartFile;
        this.tipoImmagine=tipoImmagine;
    }


    @NotNull(message = "Devi selezionare un file")
    private MultipartFile fileImmagine;

    @NotNull(message = "Seleziona un tipo di immagine (LIBRO o AUTORE)")
    private TipoImmagine tipoImmagine;

}
