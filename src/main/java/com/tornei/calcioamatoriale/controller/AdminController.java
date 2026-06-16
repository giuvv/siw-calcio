package com.tornei.calcioamatoriale.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.tornei.calcioamatoriale.model.*;
import com.tornei.calcioamatoriale.service.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private TorneoService torneoService;
    @Autowired private SquadraService squadraService;
    @Autowired private GiocatoreService giocatoreService;
    @Autowired private PartitaService partitaService;
    @Autowired private ArbitroService arbitroService;

    // ==========================================
    // DASHBOARD
    // ==========================================
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }


    // ==========================================
    // GESTIONE TORNEI
    // ==========================================
    @GetMapping("/torneo/nuovo")
    public String formNuovoTorneo(Model model) {
        model.addAttribute("torneo", new Torneo());
        model.addAttribute("tutteLeSquadre", squadraService.findAll()); 
        return "admin/form-torneo";
    }

    @GetMapping("/torneo/{id}/modifica")
    public String formModificaTorneo(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        model.addAttribute("tutteLeSquadre", squadraService.findAll());
        return "admin/form-torneo";
    }

    @PostMapping("/torneo/salva")
    public String salvaTorneo(@Valid @ModelAttribute("torneo") Torneo torneo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tutteLeSquadre", squadraService.findAll());
            return "admin/form-torneo";
        }
        torneoService.salvaTorneo(torneo);
        return "redirect:/tornei";
    }
    
    @GetMapping("/torneo/{id}/elimina")
    public String eliminaTorneo(@PathVariable Long id) {
        torneoService.eliminaTorneo(id);
        return "redirect:/tornei";
    }

    // ==========================================
    // GESTIONE SQUADRE
    // ==========================================
    @GetMapping("/squadra/nuova")
    public String formNuovaSquadra(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/form-squadra";
    }

    @GetMapping("/squadra/{id}/modifica")
    public String formModificaSquadra(@PathVariable Long id, Model model) {
        model.addAttribute("squadra", squadraService.findById(id));
        return "admin/form-squadra";
    }

    @PostMapping("/squadra/salva")
    public String salvaSquadra(@Valid @ModelAttribute("squadra") Squadra squadra, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/form-squadra";
        }
        squadraService.salvaSquadra(squadra);
        return squadra.getId() != null ? "redirect:/squadra/" + squadra.getId() : "redirect:/admin/dashboard";
    }

    @GetMapping("/squadra/{id}/elimina")
    public String eliminaSquadra(@PathVariable Long id) {
        squadraService.eliminaSquadra(id);
        return "redirect:/tornei";
    }


    // ==========================================
    // GESTIONE GIOCATORI
    // ==========================================
    @GetMapping("/giocatore/nuovo")
    public String formNuovoGiocatore(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("squadre", squadraService.findAll()); // Serve per il menu a tendina
        return "admin/form-giocatore";
    }

    @GetMapping("/giocatore/{id}/modifica")
    public String formModificaGiocatore(@PathVariable Long id, Model model) {
        model.addAttribute("giocatore", giocatoreService.findById(id));
        model.addAttribute("squadre", squadraService.findAll());
        return "admin/form-giocatore";
    }

    @PostMapping("/giocatore/salva")
    public String salvaGiocatore(@Valid @ModelAttribute("giocatore") Giocatore giocatore, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("squadre", squadraService.findAll());
            return "admin/form-giocatore";
        }
        giocatoreService.salvaGiocatore(giocatore);
        return "redirect:/squadra/" + giocatore.getSquadra().getId();
    }

    
    // ==========================================
    // GESTIONE ARBITRI
    // ==========================================
    @GetMapping("/arbitri")
    public String elencoArbitri(Model model) {
    	model.addAttribute("arbitri", arbitroService.findAll());
    	return "admin/elenco-arbitri";
    }

    @GetMapping("/arbitro/nuovo")
    public String formNuovoArbitro(Model model) {
    	model.addAttribute("arbitro", new Arbitro());
    	return "admin/form-arbitro";
    }

    @GetMapping("/arbitro/{id}/modifica")
    public String formModificaArbitro(@PathVariable Long id, Model model) {
    	model.addAttribute("arbitro", arbitroService.findById(id));
    	return "admin/form-arbitro";
    }

    @PostMapping("/arbitro/salva")
    public String salvaArbitro(@Valid @ModelAttribute("arbitro") Arbitro arbitro, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/form-arbitro";
        }
        arbitroService.salvaArbitro(arbitro);
        return "redirect:/admin/arbitri";
    }

    @GetMapping("/arbitro/{id}/elimina")
    public String eliminaArbitro(@PathVariable Long id) {
    	arbitroService.eliminaArbitro(id);
    	return "redirect:/admin/arbitri";
    }
    

    // ==========================================
    // GESTIONE PARTITE
    // ==========================================
    @GetMapping("/partita/nuova")
    public String formNuovaPartita(Model model) {
        model.addAttribute("partita", new Partita());
        model.addAttribute("tornei", torneoService.findAll());
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("arbitri", arbitroService.findAll()); // <-- aggiunta
        return "admin/form-partita";
    }

    @PostMapping("/partita/salva")
    public String salvaPartita(@Valid @ModelAttribute("partita") Partita partita, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tornei", torneoService.findAll());
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("arbitri", arbitroService.findAll());
            return "admin/form-partita";
        }
        partitaService.salvaPartita(partita);
        return "redirect:/torneo/" + partita.getTorneo().getId() + "/calendario";
    }

    @GetMapping("/partita/{id}/risultato")
    public String formRisultatoPartita(@PathVariable Long id, Model model) {
        model.addAttribute("partita", partitaService.findById(id));
        return "admin/form-risultato";
    }

    @PostMapping("/partita/{id}/risultato")
    public String salvaRisultato(@PathVariable Long id, @RequestParam Integer goalsHome, @RequestParam Integer goalsAway) {
        partitaService.inserisciRisultato(id, goalsHome, goalsAway);
        return "redirect:/partita/" + id;
    }

    @GetMapping("/partita/{id}/elimina")
    public String eliminaPartita(@PathVariable Long id) {
        Partita partita = partitaService.findById(id);
        Long torneoId = partita.getTorneo().getId();
        partitaService.eliminaPartita(id);
        // Torna al calendario del torneo
        return "redirect:/torneo/" + torneoId + "/calendario";
    }
}