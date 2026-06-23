package com.tornei.calcioamatoriale.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.model.Commento;
import com.tornei.calcioamatoriale.model.Partita;
import com.tornei.calcioamatoriale.model.Utente;
import com.tornei.calcioamatoriale.repository.CommentoRepository;
import com.tornei.calcioamatoriale.repository.PartitaRepository;
import com.tornei.calcioamatoriale.repository.UtenteRepository;

@Service
public class CommentoService {

    @Autowired private CommentoRepository commentoRepository;
    @Autowired private PartitaRepository partitaRepository;
    @Autowired private UtenteRepository utenteRepository;

    @Transactional(readOnly = true)
    public Commento findById(Long id) {
        return commentoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Commento> findByPartitaId(Long partitaId) {
        return commentoRepository.findByPartitaIdOrderByDataCreazioneDesc(partitaId);
    }

    @Transactional
    public void aggiungiCommento(Long partitaId, String testo, String username) {
        Partita partita = partitaRepository.findById(partitaId)
                .orElseThrow(() -> new RuntimeException("Partita non trovata: " + partitaId));
        Utente autore = utenteRepository.findByUsername(username);
        if (autore == null) {
            throw new RuntimeException("Utente non trovato: " + username);
        }

        Commento commento = new Commento();
        commento.setTesto(testo);
        commento.setDataCreazione(LocalDateTime.now());
        commento.setPartita(partita);
        commento.setAutore(autore);

        commentoRepository.save(commento);
    }

    @Transactional
    public void modificaCommento(Long commentoId, String nuovoTesto, String username) {
        Commento commento = commentoRepository.findById(commentoId)
                .orElseThrow(() -> new RuntimeException("Commento non trovato: " + commentoId));

        if (!commento.getAutore().getUsername().equals(username)) {
            throw new SecurityException("Non sei autorizzato a modificare questo commento");
        }

        commento.setTesto(nuovoTesto);
        commentoRepository.save(commento);
    }

    @Transactional(readOnly = true)
    public boolean isAutore(Long commentoId, String username) {
        Commento commento = commentoRepository.findById(commentoId).orElse(null);
        return commento != null 
            && commento.getAutore() != null 
            && commento.getAutore().getUsername().equals(username);
    }
}