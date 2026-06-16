package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return squadraRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Squadra findByIdWithGiocatori(Long id) {
        Squadra squadra = squadraRepository.findById(id).orElse(null);
        if (squadra != null) {
            squadra.getGiocatori().size(); 
        }
        return squadra;
    }

    @Transactional
    public void salvaSquadra(Squadra squadra) {
        squadraRepository.save(squadra);
    }

    // IL METODO CORRETTO PER L'ELIMINAZIONE SICURA
    @Transactional
    public void eliminaSquadra(Long id) {
        Squadra squadra = squadraRepository.findById(id).orElse(null);
        
        if (squadra != null) {
            // 1. Rimuoviamo la squadra dalla tabella di congiunzione dei Tornei (Many-to-Many)
            Iterable<Torneo> tornei = torneoRepository.findAll();
            for (Torneo t : tornei) {
                if (t.getSquadre() != null && t.getSquadre().contains(squadra)) {
                    t.getSquadre().remove(squadra);
                    torneoRepository.save(t);
                }
            }

            // 2. Eliminiamo tutti i giocatori iscritti a questa squadra (One-to-Many)
            if (squadra.getGiocatori() != null) {
                giocatoreRepository.deleteAll(squadra.getGiocatori());
            }

            // 3. Eliminiamo tutte le partite (calendario) in cui gioca questa squadra
            Iterable<Partita> tutteLePartite = partitaRepository.findAll();
            for (Partita p : tutteLePartite) {
                if (p.getSquadraHome().getId().equals(id) || p.getSquadraAway().getId().equals(id)) {
                    partitaRepository.delete(p);
                }
            }

            // 4. Ora che il database è pulito e non ci sono più vincoli attivi, eliminiamo la squadra
            squadraRepository.delete(squadra);
        }
    }
}