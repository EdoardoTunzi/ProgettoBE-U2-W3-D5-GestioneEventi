package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "L'username è un campo obbligatorio")
    @Size(min = 3, max = 15, message = "L'username puo avere min. 3 caratteri e max. 15")
    private String username;

    @NotBlank(message = "La password è un campo obbligatorio")
    @Size(min = 3, max = 20, message = "La password puo avere min. 3 caratteri e max. 20")
    private String password;


}
