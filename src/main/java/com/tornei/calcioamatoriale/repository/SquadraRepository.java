package com.tornei.calcioamatoriale.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.tornei.calcioamatoriale.model.Squadra;

public interface SquadraRepository extends CrudRepository<Squadra, Long> {
    @Query("SELECT s FROM Squadra s LEFT JOIN FETCH s.giocatori WHERE s.id = :id")
    Squadra findByIdWithGiocatoriEager(@Param("id") Long id);
}