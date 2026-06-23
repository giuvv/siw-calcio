package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.exception.RisorsaNonTrovataException;
import com.tornei.calcioamatoriale.model.Partita;
import com.tornei.calcioamatoriale.repository.PartitaRepository;

@Service
public class PartitaService {

    @Autowired private PartitaRepository partitaRepository;

    // READ_COMMITTED: non leggiamo risultati di partite
    // ancora non confermate da altre transazioni concorrenti.
    @Transactional
    public void salvaPartita(Partita partita) {
        partitaRepository.save(partita);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void inserisciRisultato(Long id, Integer goalsHome, Integer goalsAway) {
        if (goalsHome == null || goalsAway == null || goalsHome < 0 || goalsAway < 0) {
            throw new IllegalArgumentException("I gol non possono essere negativi.");
        }

        Partita p = partitaRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Partita non trovata con id " + id));
        p.setGoalsHome(goalsHome);
        p.setGoalsAway(goalsAway);
        p.setStato("PLAYED");
        partitaRepository.save(p);
    }

    @Transactional
    public void eliminaPartita(Long id) {
        partitaRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Partita findById(Long id) {
        return partitaRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Partita non trovata con id " + id));
    }
}