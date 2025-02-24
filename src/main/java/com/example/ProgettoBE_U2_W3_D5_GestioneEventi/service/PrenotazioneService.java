package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Evento;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Prenotazione;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.PrenotazioneDTO;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.EventoDAORepository;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.PrenotazioneDAORepository;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.UtenteDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        evento.getPrenotazioni().add(booking);

        return "Prenotazione effettuata con successo!";
    }

    public Page<PrenotazioneDTO> getAllPrenotazioni(String username) {
        Utente utente = utenteRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("Utente non trovato."));
        List<Prenotazione> listaPrenotazioni = utente.getListaPrenotazioni();

        List<PrenotazioneDTO> listPrenDTO = new ArrayList<>();

        for(Prenotazione p : listaPrenotazioni) {
            PrenotazioneDTO dto =  entity_dto(p);
            listPrenDTO.add(dto);
        }

        Page<PrenotazioneDTO> listaPage = new PageImpl<>(listPrenDTO);
        return listaPage;

    }

    public String deletePrenotazione(long idPrenotazione, String username) {
        Optional<Prenotazione> prenTrovata = prenotazioniRepo.findById(idPrenotazione);
        Utente utenteTrovato = utenteRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if(prenTrovata.isPresent()) {
            Prenotazione prenotazione = prenTrovata.get();


            if (prenotazione.getUtente().equals(utenteTrovato)) {
                prenotazioniRepo.deleteById(prenotazione.getId());
                return "Prenotazione eliminata con successo!";
            } else {
                throw new RuntimeException("Non puoi cancellare prenotazioni di altri utenti.");

            }

        } else {
            throw new RuntimeException("Errore: nessuna prenotazione trovata con questo id");
        }
    }

    public PrenotazioneDTO entity_dto(Prenotazione prenotazione) {
        PrenotazioneDTO dto = new PrenotazioneDTO();
        dto.setEvento(prenotazione.getEvento());
        dto.setUtente_Id(prenotazione.getUtente().getId());

        return dto;
    }
}
