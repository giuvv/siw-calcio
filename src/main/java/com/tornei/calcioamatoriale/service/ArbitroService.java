package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tornei.calcioamatoriale.exception.RisorsaNonTrovataException;
import com.tornei.calcioamatoriale.model.Arbitro;
import com.tornei.calcioamatoriale.repository.ArbitroRepository;

@Service
public class ArbitroService {

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Transactional(readOnly = true)
    public Iterable<Arbitro> findAll() {
        return arbitroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Arbitro findById(Long id) {
        return arbitroRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Arbitro non trovato con id " + id));
    }

    @Transactional
    public void salvaArbitro(Arbitro arbitro) {
        arbitroRepository.save(arbitro);
    }

    @Transactional
    public void eliminaArbitro(Long id) {
        arbitroRepository.deleteById(id);
    }
}