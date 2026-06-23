package com.tornei.calcioamatoriale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Gestore globale delle eccezioni.
 * Con @ControllerAdvice intercettiamo le eccezioni a livello di
 * applicazione e decidiamo noi quale pagina mostrare.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Caso specifico: una risorsa (Torneo, Squadra, Partita...) non esiste.
     * Restituiamo una pagina 404 dedicata.
     */
    @ExceptionHandler(RisorsaNonTrovataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String gestisciRisorsaNonTrovata(RisorsaNonTrovataException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/404";
    }

    /**
     * Caso generico: qualunque altra eccezione non prevista.
     * Mostriamo una pagina di errore generica.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String gestisciErroreGenerico(Exception ex, Model model) {
        ex.printStackTrace(); 
        model.addAttribute("messaggio", "Si è verificato un errore imprevisto. Riprova più tardi.");
        return "error/generico";
    }
}
