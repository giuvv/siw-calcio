package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.model.Utente;
import com.tornei.calcioamatoriale.repository.UtenteRepository;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional(readOnly = true)
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }
}