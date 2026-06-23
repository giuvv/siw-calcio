package com.tornei.calcioamatoriale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tornei.calcioamatoriale.model.Giocatore;

public interface GiocatoreRepository extends JpaRepository<Giocatore, Long> {
}