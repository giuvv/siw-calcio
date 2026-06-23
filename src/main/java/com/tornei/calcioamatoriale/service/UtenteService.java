package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.model.Utente;
import com.tornei.calcioamatoriale.repository.UtenteRepository;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }

    /**
     * Registra un nuovo utente.
     * Il ruolo è scelto liberamente da chi si registra (ADMIN o USER)
     */
    @Transactional
    public void registra(String username, String password, String ruolo) {

        if (username == null || username.trim().length() < 3) {
            throw new IllegalArgumentException("Lo username deve avere almeno 3 caratteri.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La password deve avere almeno 6 caratteri.");
        }
        if (!"ADMIN".equals(ruolo) && !"USER".equals(ruolo)) {
            throw new IllegalArgumentException("Ruolo non valido.");
        }
        if (utenteRepository.findByUsername(username.trim()) != null) {
            throw new IllegalArgumentException("Username già in uso: scegline un altro.");
        }

        Utente utente = new Utente();
        utente.setUsername(username.trim());
        utente.setPassword(passwordEncoder.encode(password));
        utente.setRuolo(ruolo);
        utenteRepository.save(utente);
    }
}