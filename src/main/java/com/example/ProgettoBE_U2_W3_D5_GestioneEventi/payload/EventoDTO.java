package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventoDTO {
    @NotBlank(message = "Il titolo è un campo obbligatorio")
    private String titolo;

    @NotBlank(message = "La descrizione è un campo obbligatorio")
    private String descrizione;

    @NotNull(message = "La data è un campo obbligatorio")
    private LocalDate data;

    @NotBlank(message = "Il luogo è un campo obbligatorio")
    private String luogo;

    @JsonProperty("nPostiDisponibili")//senza questo Jackson non riusciva a deserializzare il valore dal JSON inviato nel body
    @NotNull(message = "Numero posti disponibili è un campo obbligatorio")
    @Min(value = 1, message = "Il numero di posti disponibili deve essere min 1")
    private Integer nPostiDisponibili;

    private Long creatoreEvento_id;
}
