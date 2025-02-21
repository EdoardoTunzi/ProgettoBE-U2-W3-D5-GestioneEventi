package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteDAORepository extends JpaRepository<Utente, Long> {
    public Optional<Utente> findByUsername(String username);

    //query per la check al login
    public boolean existsByUsernameAndPassword(String username, String password);

    // check per la duplicate key
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}