package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Evento;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.EventoDTO;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.EventoDAORepository;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.UtenteDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EventoService {
    @Autowired
    EventoDAORepository eventoRepo;

    @Autowired
    UtenteDAORepository utenteRepo;

    public String saveEvento(EventoDTO eventoDTO) {
        //recupero l'organizzatore dall'id
        Utente organizzatore = utenteRepo.findById(eventoDTO.getCreatoreEvento_id())
                .orElseThrow(()-> new RuntimeException("Organizzatore non trovato"));

        //creo l'evento e assegno l'utente
        Evento eventoDaSalvare = eventoDTO_Evento(eventoDTO);
        eventoDaSalvare.setCreatoreEvento(organizzatore);

        eventoRepo.save(eventoDaSalvare);

        return "Nuovo evento salvato con successo!";
    }


    public String updateEvento(EventoDTO eventoDTO, long idEvento) {
        Optional<Evento> eventoTrovato = eventoRepo.findById(idEvento);

        if( eventoTrovato.isPresent()) {
            Evento evento = eventoTrovato.get();
            evento.setTitolo(eventoDTO.getTitolo());
            evento.setDescrizione(eventoDTO.getDescrizione());
            evento.setData(eventoDTO.getData());
            evento.setLuogo(eventoDTO.getLuogo());
            evento.setNPostiDisponibili(eventoDTO.getNPostiDisponibili());
            eventoRepo.save(evento);
        } else {
            throw new RuntimeException("Errore nella modifica dell'evento. Evento non trovato nel DB.");
        }
        return "Evento aggiornato correttamente";
    }
//aggiunto controllo sull'organizzatore che prova a cancellare l'evento. solo lo stesso organizzatore che ha creato l'evento può cancellarlo.
    public String deleteEvento(long eventoId, long organizzatoreId) {
        Optional<Evento> eventoTrovato = eventoRepo.findById(eventoId);
        Optional<Utente> organizzTrovato = utenteRepo.findById(organizzatoreId);
        if (eventoTrovato.isPresent() && organizzTrovato.isPresent()) {
            Evento evento = eventoTrovato.get();
            Utente org = organizzTrovato.get();

            if(evento.getCreatoreEvento().equals(org)) {
                eventoRepo.delete(evento);
                return "Evento rimosso con successo !";
            }else {
                throw new RuntimeException("Errore nella cancellazione dell'evento. Non hai i permessi per cancellare eventi che non hai creato tu");
            }

        } else {
            throw new RuntimeException("Errore nella cancellazione dell'evento. Nessun evento trovato con questo id");
        }
    }

    //----travaso DTO----

    public Evento eventoDTO_Evento(EventoDTO eventoDTO ) {
        Evento evento = new Evento();
        evento.setTitolo(eventoDTO.getTitolo());
        evento.setDescrizione(eventoDTO.getDescrizione());
        evento.setData(eventoDTO.getData());
        evento.setLuogo(eventoDTO.getLuogo());
        evento.setNPostiDisponibili(eventoDTO.getNPostiDisponibili());
        return evento;
    }

    public EventoDTO evento_eventoDTO(Evento evento ) {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setTitolo(evento.getTitolo());
        eventoDTO.setDescrizione(evento.getDescrizione());
        eventoDTO.setData(evento.getData());
        eventoDTO.setLuogo(evento.getLuogo());
        eventoDTO.setNPostiDisponibili(evento.getNPostiDisponibili());
        return eventoDTO;
    }
}
