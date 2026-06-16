package com.tornei.calcioamatoriale.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tornei.calcioamatoriale.model.Partita;
import com.tornei.calcioamatoriale.service.TorneoService;

/*
 * Controller REST: espone i dati del torneo in formato JSON.
 * Usato dal frontend React per caricare il calendario in modo asincrono.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private TorneoService torneoService;

    /*
     * Restituisce la lista delle partite di un torneo come array JSON.
     * Ogni partita contiene solo i campi utili al frontend
     */
    @GetMapping("/torneo/{id}/calendario")
    public List<Map<String, Object>> getCalendario(@PathVariable Long id) {
        List<Partita> partite = torneoService.getCalendario(id);

        return partite.stream().map(p -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",          p.getId());
            m.put("dataOra",     p.getDataOra() != null ? p.getDataOra().toString() : null);
            m.put("luogo",       p.getLuogo());
            m.put("squadraHome", p.getSquadraHome().getNome());
            m.put("squadraAway", p.getSquadraAway().getNome());
            m.put("goalsHome",   p.getGoalsHome());
            m.put("goalsAway",   p.getGoalsAway());
            m.put("stato",       p.getStato());
            return m;
        }).collect(Collectors.toList());
    }
}
