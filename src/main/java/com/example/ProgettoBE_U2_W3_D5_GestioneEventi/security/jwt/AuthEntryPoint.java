package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Implementiamo l'interfaccia che rileva eventuali errori di autenticazione
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> infoErrori = new HashMap<>();
        infoErrori.put("stato", HttpServletResponse.SC_UNAUTHORIZED);
        infoErrori.put("errore", "Autorizzazione non valida");
        infoErrori.put("messaggio", authException.getMessage());
        infoErrori.put("path", request.getServletPath());

        final ObjectMapper mappaturaErrori = new ObjectMapper();
        mappaturaErrori.writeValue(response.getOutputStream(), infoErrori);
    }
}
