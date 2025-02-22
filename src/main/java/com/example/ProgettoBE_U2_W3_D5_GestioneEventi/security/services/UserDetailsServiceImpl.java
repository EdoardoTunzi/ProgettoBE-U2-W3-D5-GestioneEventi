package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.services;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository.UtenteDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UtenteDAORepository repoUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utente> utente = repoUser.findByUsername(username);
        Utente user = utente.orElseThrow();
        return UserDetailsImpl.costruisciDettagli(user);
    }
}
