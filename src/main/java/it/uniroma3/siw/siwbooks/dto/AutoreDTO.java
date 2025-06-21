package it.uniroma3.siw.siwbooks.dto;

import it.uniroma3.siw.siwbooks.model.Autore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoreDTO {
    private Long id;
    private String nome;
    // getters/setters

    public static AutoreDTO fromEntity(Autore a) {
        AutoreDTO dto = new AutoreDTO();
        dto.setId(a.getId());
        dto.setNome(a.getNome());
        return dto;
    }
    
}
