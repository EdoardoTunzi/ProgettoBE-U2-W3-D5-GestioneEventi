package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.EmailDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.UsernameDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Eruolo;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Ruolo;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.UtenteDTO;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.request.RegistrazioneRequest;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.RuoloRepository;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.UtenteDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class UtenteService {

    @Autowired
    UtenteDAORepository utenteRepo;

    @Autowired
    private RuoloRepository ruoloRepository;

    // registrazione nuovo utente
    public String inserisciUtente(RegistrazioneRequest registrazione) throws UsernameDuplicateException, EmailDuplicateException {
        checkDuplicateKey(registrazione.getUsername(), registrazione.getEmail());
        Utente user = registrazioneRequest_Utente(registrazione);
        Long id = utenteRepo.save(user).getId();
        return "Nuovo utente " + user.getUsername() + "con id " + id + " è stato inserito correttamente";
    }

    //metodo per controllo duplicateKey

    public void checkDuplicateKey(String username, String email) throws UsernameDuplicateException, EmailDuplicateException {
        if (utenteRepo.existsByUsername(username)) {
            throw new UsernameDuplicateException("Username già utilizzato, non disponibile");
        }

        if (utenteRepo.existsByEmail(email)) {
            throw new EmailDuplicateException("Email già utilizzata da un altro utente ");
        }
    }

    //metodi travaso

    public Utente dto_entity(UtenteDTO dto) {
        Utente utente = new Utente();
        utente.setEmail(dto.getEmail());
        utente.setNome(dto.getNome());
        utente.setUsername(dto.getUsername());
        utente.setCognome(dto.getCognome());
        utente.setPassword(dto.getPassword());
        return utente;

    }

    public UtenteDTO entity_dto(Utente utente) {
        UtenteDTO dto = new UtenteDTO();
        dto.setEmail(utente.getEmail());
        dto.setNome(utente.getNome());
        dto.setUsername(utente.getUsername());
        dto.setCognome(utente.getCognome());
        return dto;

    }

    //travaso da RegistrazioneRequest a entity Utente -FINISCI CONTROLLO RUOLI!!!!!!

    public Utente registrazioneRequest_Utente (RegistrazioneRequest request) {
        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setNome(request.getNome());
        utente.setUsername(request.getUsername());
        utente.setCognome(request.getCognome());
        utente.setPassword(request.getPassword());

        if (request.getRuolo() == null) {
            Ruolo defaultRole = ruoloRepository.findByNome(Eruolo.ROLE_USER).orElseThrow(() -> new RuntimeException("Errore: Ruolo non trovato."));
            utente.setRuolo(defaultRole);
        } else if(request.getRuolo().equals(Eruolo.ROLE_ORGANIZZATORE.name())){
            Ruolo organizzatoreRole = ruoloRepository.findByNome(Eruolo.ROLE_ORGANIZZATORE).orElseThrow(() -> new RuntimeException("Errore: Ruolo non trovato."));
            utente.setRuolo(organizzatoreRole);
        } else if (request.getRuolo().equals(Eruolo.ROLE_ADMIN.name())) {
            Ruolo adminRole = ruoloRepository.findByNome(Eruolo.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Errore: Ruolo non trovato."));
            utente.setRuolo(adminRole);
        } else {
            throw new RuntimeException("Errore: Il Valore inserito come ruolo non è valido!");
        }

        return utente;
    }
}
