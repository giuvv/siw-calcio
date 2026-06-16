package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.model.Partita;
import com.tornei.calcioamatoriale.repository.PartitaRepository;

@Service
public class PartitaService {

    @Autowired private PartitaRepository partitaRepository;

    @Transactional
    public void salvaPartita(Partita partita) {
        partitaRepository.save(partita);
    }

    @Transactional
    public void inserisciRisultato(Long id, Integer goalsHome, Integer goalsAway) {
        Partita p = partitaRepository.findById(id).orElseThrow();
        p.setGoalsHome(goalsHome);
        p.setGoalsAway(goalsAway);
        p.setStato("PLAYED"); // La partita passa allo stato "giocata"
        partitaRepository.save(p);
    }

    @Transactional
    public void eliminaPartita(Long id) {
        partitaRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Partita findById(Long id) {
        return partitaRepository.findById(id).orElse(null);
    }
}