package com.tornei.calcioamatoriale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tornei.calcioamatoriale.model.Arbitro;

public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
}