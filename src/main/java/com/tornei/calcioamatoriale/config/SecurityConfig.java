package com.tornei.calcioamatoriale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// Dichiariamo quale algoritmo usare per le password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. Pagine pubbliche in LETTURA (Tutti possono vederle)
                .requestMatchers(HttpMethod.GET,
                        "/", "/tornei", "/torneo/**", "/squadra/**", "/partita/**",
                        "/api/**", "/error", "/css/**", "/js/**", "/registrazione").permitAll()

                // 1bis. Invio del form di registrazione: pubblico per definizione
                // (chi si registra non è ancora autenticato).
                .requestMatchers(HttpMethod.POST, "/registrazione").permitAll()

                // 2. Inserimento di un nuovo commento: richiede login.
                //    IMPORTANTE: va dichiarato PRIMA della regola generica su /admin e /commento,
                //    perché Spring Security applica la prima regola che combacia.
                .requestMatchers(HttpMethod.POST, "/partita/*/commento").hasAnyRole("USER", "ADMIN")

                // 3. Pagine Amministratore (Solo chi ha ruolo ADMIN)
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 4. Modifica dei commenti (Aperto a USER e ADMIN, poi il controller verifica la paternità)
                .requestMatchers("/commento/**").hasAnyRole("USER", "ADMIN")

                // Tutto il resto richiede il login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/tornei", true) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/tornei")
                .permitAll()
            );

        return http.build();
    }
}