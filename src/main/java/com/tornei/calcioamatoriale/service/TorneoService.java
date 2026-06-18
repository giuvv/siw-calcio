package com.tornei.calcioamatoriale.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.exception.RisorsaNonTrovataException;
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

    // Lancia RisorsaNonTrovataException se l'id non esiste, invece di restituire null.
    // Così chi chiama questo metodo (controller, altri service) non deve fare
    // controlli null sparsi ovunque: se il torneo non c'è, il flusso si interrompe
    // qui in modo esplicito e viene gestito centralmente da GlobalExceptionHandler.
    @Transactional(readOnly = true)
    public Torneo findById(Long id) {
        Torneo torneo = torneoRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Torneo non trovato con id " + id));
        torneo.getSquadre().size(); // inizializza la collezione LAZY dentro la transazione
        return torneo;
    }

    @Transactional(readOnly = true)
    public List<Partita> getCalendario(Long torneoId) {
        return partitaRepository.findByTorneoId(torneoId);
    }

    // READ_COMMITTED: calcoliamo la classifica su risultati già confermati.
    // non leggiamo goal inseriti da una transazione ancora non chiusa
    // (es. un admin che sta modificando un risultato nello stesso istante).
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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

        // Ordina per punti decrescenti
        return classifica.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    @Transactional
    public void salvaTorneo(Torneo torneo) {
        torneoRepository.save(torneo);
    }
    
    @Transactional
    public void eliminaTorneo(Long id) {
        // Prima eliminiamo le partite associate
        List<Partita> partite = partitaRepository.findByTorneoId(id);
        partitaRepository.deleteAll(partite);
        torneoRepository.deleteById(id);
    }
}
