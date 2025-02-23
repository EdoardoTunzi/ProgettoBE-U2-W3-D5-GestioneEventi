package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Evento;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Prenotazione;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.EventoDAORepository;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.PrenotazioneDAORepository;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.UtenteDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrenotazioneService {

    @Autowired
    EventoDAORepository eventoRepo;

    @Autowired
    PrenotazioneDAORepository prenotazioniRepo;

    @Autowired
    UtenteDAORepository utenteRepo;

    public String bookingEvento(long eventoId, String username) {
        Utente utente = utenteRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("Utente non trovato."));

        Evento evento = eventoRepo.findById(eventoId).orElseThrow(()-> new RuntimeException("Evento non trovato"));

        if ((evento.getNPostiDisponibili() == 0)) {
            throw new RuntimeException("Non ci sono più posti disponibili per questo evento");
        }

        evento.decrementaPostiDisponibili();
        Prenotazione booking = new Prenotazione();
        booking.setUtente(utente);
        booking.setEvento(evento);
        prenotazioniRepo.save(booking);
        utente.getListaPrenotazioni().add(booking);

        return "Prenotazione effettuata con successo!";
    }
}
