package com.tornei.calcioamatoriale.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.model.Partita;
import com.tornei.calcioamatoriale.model.Squadra;
import com.tornei.calcioamatoriale.model.Torneo;
import com.tornei.calcioamatoriale.repository.PartitaRepository;
import com.tornei.calcioamatoriale.repository.TorneoRepository;

@Service
public class TorneoService {

    @Autowired private TorneoRepository torneoRepository;
    @Autowired private PartitaRepository partitaRepository;

    @Transactional(readOnly = true)
    public Iterable<Torneo> findAll() {
        return torneoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Torneo findById(Long id) {
        Torneo torneo = torneoRepository.findById(id).orElse(null);
        if (torneo != null) {
            torneo.getSquadre().size(); // Inizializza la collezione Lazy delle squadre
        }
        return torneo;
    }

    @Transactional(readOnly = true)
    public List<Partita> getCalendario(Long torneoId) {
        return partitaRepository.findByTorneoId(torneoId); // Assicurati di avere questo metodo nel PartitaRepository!
    }

    // Calcolo automatico della classifica
    @Transactional(readOnly = true)
    public Map<String, Integer> getClassifica(Long torneoId) {
        Torneo torneo = findById(torneoId);
        List<Partita> partite = getCalendario(torneoId);
        Map<String, Integer> classifica = new HashMap<>();

        if (torneo != null && torneo.getSquadre() != null) {
            for (Squadra s : torneo.getSquadre()) {
                classifica.put(s.getNome(), 0);
            }
        }

        for (Partita p : partite) {
            if ("PLAYED".equals(p.getStato()) && p.getGoalsHome() != null && p.getGoalsAway() != null) {
                String sqHome = p.getSquadraHome().getNome();
                String sqAway = p.getSquadraAway().getNome();
                
                if (p.getGoalsHome() > p.getGoalsAway()) {
                    classifica.put(sqHome, classifica.getOrDefault(sqHome, 0) + 3);
                } else if (p.getGoalsHome() < p.getGoalsAway()) {
                    classifica.put(sqAway, classifica.getOrDefault(sqAway, 0) + 3);
                } else {
                    classifica.put(sqHome, classifica.getOrDefault(sqHome, 0) + 1);
                    classifica.put(sqAway, classifica.getOrDefault(sqAway, 0) + 1);
                }
            }
        }
        return classifica;
    }
    
    @Transactional
    public void salvaTorneo(Torneo torneo) {
        torneoRepository.save(torneo);
    }
    
    @Transactional
    public void eliminaTorneo(Long id) {
        torneoRepository.deleteById(id);
    }
}