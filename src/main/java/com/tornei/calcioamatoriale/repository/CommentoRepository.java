package com.tornei.calcioamatoriale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tornei.calcioamatoriale.model.Commento;

public interface CommentoRepository extends JpaRepository<Commento, Long> {
    // Estrae tutti i commenti di una partita dal più recente al più vecchio
    List<Commento> findByPartitaIdOrderByDataCreazioneDesc(Long partitaId);
}