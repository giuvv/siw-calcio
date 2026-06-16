package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.model.Giocatore;
import com.tornei.calcioamatoriale.repository.GiocatoreRepository;

@Service
public class GiocatoreService {

    @Autowired private GiocatoreRepository giocatoreRepository;

    @Transactional
    public void salvaGiocatore(Giocatore giocatore) {
        giocatoreRepository.save(giocatore);
    }
    
    @Transactional(readOnly = true)
    public Giocatore findById(Long id) {
        return giocatoreRepository.findById(id).orElse(null);
    }
}