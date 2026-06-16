package com.tornei.calcioamatoriale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tornei.calcioamatoriale.model.Utente;
import com.tornei.calcioamatoriale.repository.UtenteRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username);
        
        if (utente == null) {
            throw new UsernameNotFoundException("Utente non trovato");
        }

        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPassword())
                .roles(utente.getRuolo())       // "USER" o "ADMIN"
                .build();
    }
}