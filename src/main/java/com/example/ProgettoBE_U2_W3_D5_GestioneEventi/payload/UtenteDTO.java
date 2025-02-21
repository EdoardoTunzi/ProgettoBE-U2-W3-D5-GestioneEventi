package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UtenteDTO {

    private String nome;
    private String cognome;

    @NotBlank(message = "L'username è un campo obbligatorio")
    private String username;
    @NotBlank(message = "La password è un campo obbligatorio")
    private String password;
    @NotBlank(message = "L'email è un campo obbligatorio")
    @Email(message = "Il formato dell'indirizzo email non è valido")
    private String email;
}
