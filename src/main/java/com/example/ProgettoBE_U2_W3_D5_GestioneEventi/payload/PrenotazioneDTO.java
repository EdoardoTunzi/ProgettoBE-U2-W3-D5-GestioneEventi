package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Evento;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrenotazioneDTO {
    @NotBlank(message = "L'utente è un campo obbligatorio")
    private Utente utente;

    @NotBlank(message = "L'evento è un campo obbligatorio")
    private Evento evento;
}
