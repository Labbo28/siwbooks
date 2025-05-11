package it.uniroma3.siw.siwbooks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDTO {


    @NotBlank(message = "L'email è obbligatoria")
    private String email;
    @NotBlank(message = "La password è obbligatoria")
     @Size(min = 8, message = "La password deve essere di almeno 8 caratteri")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$", 
     message = "La password deve contenere almeno un numero, una lettera minuscola e una maiuscola")
    private String password;
}
