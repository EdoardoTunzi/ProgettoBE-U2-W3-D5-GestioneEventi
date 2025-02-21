package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.controller;


import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.EmailDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.UsernameDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.UtenteDTO;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utente")
public class UtenteController {
    @Autowired
    UtenteService utenteService;

    //creazione/registrazione nuovo utente
    @PostMapping("/new")
    public ResponseEntity<String> inserisciUtente(@Validated @RequestBody UtenteDTO nuovoUtente, BindingResult validazione){

        try {
            if(validazione.hasErrors()){
                StringBuilder errori = new StringBuilder("Problemi nella validazione dati :\n");

                for(ObjectError errore : validazione.getAllErrors()){
                    errori.append(errore.getDefaultMessage()).append("\n");
                }
                return new ResponseEntity<>(errori.toString(), HttpStatus.BAD_REQUEST);
            }
            String messaggio =utenteService.inserisciUtente(nuovoUtente);
            return new ResponseEntity<>(messaggio, HttpStatus.OK);
        } catch (UsernameDuplicateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EmailDuplicateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
