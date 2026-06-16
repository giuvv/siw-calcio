package com.tornei.calcioamatoriale.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.tornei.calcioamatoriale.model.Partita;

public interface PartitaRepository extends CrudRepository<Partita, Long> {
    // Trova tutte le partite di un determinato torneo (serve per il calendario)
    List<Partita> findByTorneoId(Long torneoId);
}