package com.tornei.calcioamatoriale.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// Dichiariamo quale algoritmo usare per le password (lo stesso usato nel DataInitializer)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. Pagine pubbliche (Tutti possono vederle)
                .requestMatchers("/", "/tornei", "/torneo/**", "/squadra/**", "/partita/**", "/error", "/css/**", "/js/**").permitAll()
                
                // 2. Pagine Amministratore (Solo chi ha ruolo ADMIN)
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 3. Modifica dei commenti (Aperto a USER e ADMIN, poi il controller verifica la paternità)
                .requestMatchers("/commento/**").hasAnyRole("USER", "ADMIN")
                
                // Tutto il resto richiede il login (incluso il POST per creare nuovi commenti)
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