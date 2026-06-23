package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.exception.RisorsaNonTrovataException;
import com.tornei.calcioamatoriale.model.Partita;
import com.tornei.calcioamatoriale.model.Squadra;
import com.tornei.calcioamatoriale.model.Torneo;
import com.tornei.calcioamatoriale.repository.GiocatoreRepository;
import com.tornei.calcioamatoriale.repository.PartitaRepository;
import com.tornei.calcioamatoriale.repository.SquadraRepository;
import com.tornei.calcioamatoriale.repository.TorneoRepository;

@Service
public class SquadraService {

    @Autowired private SquadraRepository squadraRepository;
    @Autowired private TorneoRepository torneoRepository;
    @Autowired private GiocatoreRepository giocatoreRepository;
    @Autowired private PartitaRepository partitaRepository;

    @Transactional(readOnly = true)
    public Iterable<Squadra> findAll() {
        return squadraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Squadra findById(Long id) {
        return squadraRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Squadra non trovata con id " + id));
    }
    
    // ANALISI PRESTAZIONI
    // Confronto tra due strategie di fetch JPA

    /*
     * STRATEGIA 1 — Fetch LAZY (causa il problema N+1).
     *
     * Hibernate esegue DUE query SQL separate:
     *   1. SELECT * FROM squadra WHERE id = ?
     *   2. SELECT * FROM giocatore WHERE squadra_id = ?   ← scatenata da .size()
     *
     * Con N squadre, questo diventerebbe N+1 query (1 per le squadre + N per i giocatori).
     * Con dataset grandi, il costo è significativo.
     */
    @Transactional(readOnly = true)
    public Squadra findByIdConGiocatoriLazy(Long id) {
        Squadra squadra = squadraRepository.findById(id).orElse(null);
        if (squadra != null) {
            squadra.getGiocatori().size(); // forza il caricamento lazy → seconda query
        }
        return squadra;
    }

    /**
     * STRATEGIA 2 — JOIN FETCH via JPQL (1 sola query).
     *
     * Hibernate esegue UNA sola query SQL:
     *   SELECT s.*, g.* FROM squadra s
     *   LEFT JOIN giocatore g ON g.squadra_id = s.id
     *   WHERE s.id = ?
     *
     * Squadra e giocatori vengono caricati insieme, eliminando il problema N+1.
     * Questa è la strategia scelta per la produzione.
     */
    @Transactional(readOnly = true)
    public Squadra findByIdConGiocatoriJoinFetch(Long id) {
        return squadraRepository.findByIdWithGiocatoriEager(id);
    }

    /**
     * Metodo usato dal controller pubblico: usa la strategia ottimizzata (JOIN FETCH).
     */
    @Transactional(readOnly = true)
    public Squadra findByIdWithGiocatori(Long id) {
        Squadra squadra = findByIdConGiocatoriJoinFetch(id);
        if (squadra == null) {
            throw new RisorsaNonTrovataException("Squadra non trovata con id " + id);
        }
        return squadra;
    }

    @Transactional
    public void salvaSquadra(Squadra squadra) {
        squadraRepository.save(squadra);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void eliminaSquadra(Long id) {
        Squadra squadra = squadraRepository.findById(id).orElse(null);
        
        if (squadra != null) {
            // 1. Rimuove la squadra dalla tabella di congiunzione dei Tornei
            Iterable<Torneo> tornei = torneoRepository.findAll();
            for (Torneo t : tornei) {
                if (t.getSquadre() != null && t.getSquadre().contains(squadra)) {
                    t.getSquadre().remove(squadra);
                    torneoRepository.save(t);
                }
            }

            // 2. Elimina tutti i giocatori iscritti
            if (squadra.getGiocatori() != null) {
                giocatoreRepository.deleteAll(squadra.getGiocatori());
            }

            // 3. Elimina le partite in cui gioca questa squadra
            Iterable<Partita> tutteLePartite = partitaRepository.findAll();
            for (Partita p : tutteLePartite) {
                if (p.getSquadraHome().getId().equals(id) || p.getSquadraAway().getId().equals(id)) {
                    partitaRepository.delete(p);
                }
            }

            // 4. Elimina la squadra
            squadraRepository.delete(squadra);
        }
    }
}
