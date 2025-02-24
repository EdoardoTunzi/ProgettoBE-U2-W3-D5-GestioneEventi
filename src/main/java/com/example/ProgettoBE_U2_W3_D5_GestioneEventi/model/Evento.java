package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "eventi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private String luogo;

    @Column(nullable = false)
    private int nPostiDisponibili;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    @JsonIgnore
    private Utente creatoreEvento;

    @OneToMany
    @JsonIgnore
    private List<Prenotazione> prenotazioni;

    public void decrementaPostiDisponibili() {
        if (nPostiDisponibili > 0) {
            nPostiDisponibili --;
        } else {
            throw new RuntimeException("Questo evento è sold out!");
        }
    }
  }
