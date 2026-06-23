package com.tornei.calcioamatoriale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tornei.calcioamatoriale.model.Torneo;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
}