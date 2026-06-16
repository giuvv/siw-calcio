package com.tornei.calcioamatoriale.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tornei.calcioamatoriale.service.SquadraService;
import com.tornei.calcioamatoriale.service.TorneoService;

@Controller
public class PublicController {

    @Autowired private TorneoService torneoService;
    @Autowired private SquadraService squadraService;

    // 1. Elenco Tornei
    @GetMapping({"/", "/tornei"})
    public String elencoTornei(Model model) {
        model.addAttribute("tornei", torneoService.findAll());
        return "tornei"; // Pagina tornei.html
    }

    // 2. Dettaglio Torneo (include l'elenco delle squadre)
    @GetMapping("/torneo/{id}")
    public String dettaglioTorneo(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        return "torneo"; // Pagina torneo.html
    }

    // 3. Calendario Partite
    @GetMapping("/torneo/{id}/calendario")
    public String calendarioTorneo(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        model.addAttribute("partite", torneoService.getCalendario(id));
        return "calendario"; // Pagina calendario.html
    }

    // 4. Classifica Torneo
    @GetMapping("/torneo/{id}/classifica")
    public String classificaTorneo(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        model.addAttribute("classifica", torneoService.getClassifica(id));
        return "classifica"; // Pagina classifica.html
    }

    // 5. Dettaglio Squadra con Giocatori
    @GetMapping("/squadra/{id}")
    public String dettaglioSquadra(@PathVariable Long id, Model model) {
        model.addAttribute("squadra", squadraService.findByIdWithGiocatori(id));
        return "squadra"; // Pagina squadra.html
    }
    
    @GetMapping("/login")
    public String mostraPaginaLogin() {
        return "login"; // Questo dice a Spring di caricare il tuo file login.html
    }
}