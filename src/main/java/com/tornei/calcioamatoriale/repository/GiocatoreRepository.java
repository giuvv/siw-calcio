package com.tornei.calcioamatoriale.repository;

import org.springframework.data.repository.CrudRepository;
import com.tornei.calcioamatoriale.model.Giocatore;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {
}