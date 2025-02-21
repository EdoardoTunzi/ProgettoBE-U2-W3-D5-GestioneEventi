package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.repository;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoDAORepository extends JpaRepository<Evento, Long> {
}
