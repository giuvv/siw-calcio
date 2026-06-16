package com.tornei.calcioamatoriale.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tornei.calcioamatoriale.model.*;
import com.tornei.calcioamatoriale.repository.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UtenteRepository utenteRepository;
    @Autowired private TorneoRepository torneoRepository;
    @Autowired private SquadraRepository squadraRepository;
    @Autowired private PartitaRepository partitaRepository;
    @Autowired private ArbitroRepository arbitroRepository;
    @Autowired private PasswordEncoder passwordEncoder; 

    @Override
    public void run(String... args) throws Exception {
        
        if (utenteRepository.count() == 0) {
            // 1. Creiamo l'Amministratore
            Utente admin = new Utente();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRuolo("ADMIN");
            utenteRepository.save(admin);

            // 2. Creiamo l'Utente normale (per i commenti)
            Utente user = new Utente();
            user.setUsername("mario.rossi");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRuolo("USER");
            utenteRepository.save(user);
            
            System.out.println("=== UTENTI CREATI: Admin e User ===");
        }

        // Arbitri
        if (arbitroRepository.count() == 0) {
            Arbitro a1 = new Arbitro();
            a1.setNome("Marco");
            a1.setCognome("Bianchi");
            a1.setCodiceArbitrale("ARB-001");
            arbitroRepository.save(a1);
            
            Arbitro a2 = new Arbitro();
            a2.setNome("Luca");
            a2.setCognome("Verdi");
            a2.setCodiceArbitrale("ARB-002");
            arbitroRepository.save(a2);
            
            System.out.println("=== ARBITRI DI PROVA INSERITI ===");
        }
        
        // Tornei e Squadre
        if (torneoRepository.count() == 0) {
            
            Squadra s1 = new Squadra();
            s1.setNome("Raptors FC");
            s1.setCitta("Roma");
            s1.setAnnoFondazione(2020);
            squadraRepository.save(s1);

            Squadra s2 = new Squadra();
            s2.setNome("Trastevere City");
            s2.setCitta("Roma");
            s2.setAnnoFondazione(2018);
            squadraRepository.save(s2);

            Torneo torneo = new Torneo();
            torneo.setNome("Champions League Amatoriale");
            torneo.setAnno(2026);
            torneo.setDescrizione("Il torneo più prestigioso della capitale.");
            
            torneo.setSquadre(new java.util.ArrayList<>());
            torneo.getSquadre().add(s1);
            torneo.getSquadre().add(s2);
            torneoRepository.save(torneo);

            Partita p1 = new Partita();
            p1.setTorneo(torneo);
            p1.setSquadraHome(s1);
            p1.setSquadraAway(s2);
            p1.setDataOra(java.time.LocalDateTime.now().plusDays(2));
            p1.setLuogo("Campo San Francesco");
            p1.setStato("SCHEDULED");
            partitaRepository.save(p1);
            
            System.out.println("=== DATI DI PROVA INSERITI CON SUCCESSO ===");
        }
    }
}