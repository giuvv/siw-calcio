package com.tornei.calcioamatoriale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tornei.calcioamatoriale.model.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByUsername(String username);
}