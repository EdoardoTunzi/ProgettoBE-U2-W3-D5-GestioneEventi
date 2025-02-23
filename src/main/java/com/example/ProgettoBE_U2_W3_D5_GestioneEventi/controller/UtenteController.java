package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.controller;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.EventoDTO;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.UtenteDAORepository;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service.EventoService;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service.PrenotazioneService;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.service.UtenteService;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.EmailDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.exception.UsernameDuplicateException;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.request.LoginRequest;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.request.RegistrazioneRequest;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.payload.response.JwtResponse;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.jwt.JwtUtils;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utente")
public class UtenteController {
//non so perchè ma mi da errore il compiler,
// comunque anche se in rosso, controller legge la classe UtenteService. Ho provato a copiare, cancellare e incollare tutta
// la classe service e per poco toglie l'errore, ma poi ritorna.
    @Autowired
    UtenteService utenteService;

    @Autowired
    AuthenticationManager managerAuth;

    @Autowired
    JwtUtils utilitiesJwt;

    @Autowired
    EventoService eventoService;

    @Autowired
    UtenteDAORepository utenteRepo;

    @Autowired
    PrenotazioneService prenotazioneService;

    //creazione/registrazione nuovo utente
    @PostMapping("/new")
    public ResponseEntity<String> signUp(@Validated @RequestBody RegistrazioneRequest nuovoUtente, BindingResult validazione){

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginDto, BindingResult validazione){

        // VALIDAZIONE
        if(validazione.hasErrors()){
            StringBuilder errori = new StringBuilder("Problemi nella validazione dati :\n");

            for(ObjectError errore : validazione.getAllErrors()){
                errori.append(errore.getDefaultMessage()).append("\n");
            }

            return new ResponseEntity<>(errori.toString(), HttpStatus.BAD_REQUEST);

        }

        try {
            //Generiamo un oggetto che occorre per l'autenticazione
            UsernamePasswordAuthenticationToken tokenNoAuth = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            // Invocare e a recuperare l'authentication -> autenticazione va a buon fine
            // Utilizziamo il gestore delle autenticazioni che si basa su Useername e Password
            // Recuperiamo l'autenticazione attraverso il metodo authenticate
            Authentication autenticazione = managerAuth.authenticate(tokenNoAuth);

            // Impostare l'autenticazione nel contesto di sicurezza Spring
            SecurityContextHolder.getContext().setAuthentication(autenticazione);

            // Generiamo il TOKEN FINALE (String)
            String token = utilitiesJwt.creaJwtToken(autenticazione);

            // Recuperando le info che vogliamo inserire nella risposta al client
            UserDetailsImpl dettagliUtente = (UserDetailsImpl) autenticazione.getPrincipal();
            List<String> ruoliweb = dettagliUtente.getAuthorities().stream()
                    .map((item->item.getAuthority()))
                    .collect(Collectors.toList());

            // Creare un oggetto JWTresponse
            JwtResponse responseJWT = new JwtResponse(dettagliUtente.getUsername(), dettagliUtente.getId(),dettagliUtente.getEmail() , ruoliweb, token);

            // Gestione della risposta al Client -> ResponseEntity
            return new ResponseEntity<>(responseJWT, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Credenziali non valide", HttpStatus.UNAUTHORIZED);
        }

    }

    //-------Metodi organizzatore-----

    @PostMapping("/org/newEvento")
    @PreAuthorize("hasAnyAuthority('ROLE_ORGANIZZATORE')")
    public ResponseEntity<?> creaEvento (@Validated @RequestBody EventoDTO eventoDTO, BindingResult validazione,Authentication authentication ) {

        if(validazione.hasErrors()){
            StringBuilder errori = new StringBuilder("Problemi nella validazione dati :\n");

            for(ObjectError errore : validazione.getAllErrors()){
                errori.append(errore.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(errori.toString(), HttpStatus.BAD_REQUEST);
        }

        String usernameOrg = authentication.getName();
        Utente organizzatore = utenteRepo.findByUsername(usernameOrg)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        eventoDTO.setCreatoreEvento_id(organizzatore.getId());


        String messaggio = eventoService.saveEvento(eventoDTO);
        return new ResponseEntity<>(messaggio, HttpStatus.OK);
    }

    //------metodi user normali----

    @PostMapping("/booking/{eventoId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> creaPrenotazioneEvento(@PathVariable long eventoId, Authentication authentication) {
        try {
            String username = authentication.getName();
            String messaggio = prenotazioneService.bookingEvento(eventoId, username);
            return new ResponseEntity<>( messaggio, HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
