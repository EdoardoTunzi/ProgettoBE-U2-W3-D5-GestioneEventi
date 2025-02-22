package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Eruolo;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    Optional<Ruolo> findByNome(Eruolo nome);
}
