package com.tornei.calcioamatoriale.repository;

import org.springframework.data.repository.CrudRepository;
import com.tornei.calcioamatoriale.model.Torneo;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {
}