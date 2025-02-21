package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.EmailDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.UsernameDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.UtenteDTO;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.UtenteDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UtenteService {
    @Autowired
    UtenteDAORepository utenteRepo;

    // registrazione nuovo utente
    public String inserisciUtente(UtenteDTO utenteDTO) throws UsernameDuplicateException, EmailDuplicateException {
        checkDuplicateKey(utenteDTO.getUsername(), utenteDTO.getEmail());
        Utente user = dto_entity(utenteDTO);
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
}
