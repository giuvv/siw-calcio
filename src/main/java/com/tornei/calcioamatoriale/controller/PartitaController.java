package com.tornei.calcioamatoriale.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tornei.calcioamatoriale.model.Commento;
import com.tornei.calcioamatoriale.service.CommentoService;
import com.tornei.calcioamatoriale.service.PartitaService;

@Controller
public class PartitaController {

    @Autowired private PartitaService partitaService;
    @Autowired private CommentoService commentoService;

    // 1. Dettaglio partita e commenti
    @GetMapping("/partita/{id}")
    public String dettaglioPartita(@PathVariable Long id, Model model) {
        model.addAttribute("partita", partitaService.findById(id));
        model.addAttribute("commenti", commentoService.findByPartitaId(id));
        return "partita";
    }

    // 2. Inserimento nuovo commento
    @PostMapping("/partita/{id}/commento")
    public String aggiungiCommento(@PathVariable Long id, @RequestParam String testo) {
        String username = getUsernameLoggato();
        commentoService.aggiungiCommento(id, testo, username);
        return "redirect:/partita/" + id;
    }

    // 3. Form di modifica commento
    @GetMapping("/commento/{id}/modifica")
    public String formModificaCommento(@PathVariable Long id, Model model) {
        String username = getUsernameLoggato();
        Commento commento = commentoService.findById(id);
        
        if (commento == null) {
            return "redirect:/tornei";
        }
        
        // Se non sei l'autore, vieni rimandato indietro
        if (!commentoService.isAutore(id, username)) {
            return "redirect:/partita/" + commento.getPartita().getId();
        }

        model.addAttribute("commento", commento);
        return "modifica-commento";
    }

    // 4. Salvataggio modifica commento
    @PostMapping("/commento/{id}/modifica")
    public String salvaModificaCommento(@PathVariable Long id, @RequestParam String testo) {
        String username = getUsernameLoggato();
        Commento commento = commentoService.findById(id);
        
        if (commento == null) {
            return "redirect:/tornei";
        }
        Long partitaId = commento.getPartita().getId();

        try {
            commentoService.modificaCommento(id, testo, username);
        } catch (SecurityException e) {
        }

        return "redirect:/partita/" + partitaId;
    }

    private String getUsernameLoggato() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}