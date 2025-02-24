package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.jwt;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


// Funzionlità Utilities del TOKEN
@Component
public class JwtUtils {

    // Aggancio le costanti legate al JWT
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirations;

    // Creazione del JWT
    public String creaJwtToken(Authentication autenticazione){

        // Recupero il dettaglio principal (username)
        UserDetailsImpl utentePrincipal = (UserDetailsImpl) autenticazione.getPrincipal();//il metodo getDetails era sbagliato!

        // Creazione del JWT
        return Jwts.builder()
                .setSubject(utentePrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpirations))
                .signWith(SignatureAlgorithm.HS256, recuperoChiave())
                .compact();
    }

    // Recupera l'username dal JWT .parseClaimsJwt era sbagliato e mi bloccava la creazione dell'evento.
    public String recuperoUsernameDaToken(String token){
        return Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Recupera la scadenza dal JWT -
    public Date recuperoScadenzaDaToken(String token){
        return Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parseClaimsJws(token).getBody().getExpiration();
    }

    // Validazione del TOKEN JWT
    public boolean validazioneJwtToken(String token){
        Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parse(token);
        return true;
    }

    // Recupero della chiave
    public Key recuperoChiave(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}