package com.tornei.calcioamatoriale.repository;

import org.springframework.data.repository.CrudRepository;
import com.tornei.calcioamatoriale.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
    // Spring Boot capisce da solo cosa fare leggendo il nome del metodo!
    Utente findByUsername(String username);
}