package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idruolo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Eruolo nome;


}
