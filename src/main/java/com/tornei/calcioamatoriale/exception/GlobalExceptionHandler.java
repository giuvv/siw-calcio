package com.tornei.calcioamatoriale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Gestore globale delle eccezioni.
 *
 * Senza questa classe, qualunque eccezione non gestita nei controller (ad es.
 * una NullPointerException causata da un id inesistente) arriva fino a Spring
 * Boot, che la trasforma nella sua Whitelabel Error Page generica — poco
 * professionale e poco informativa per chi naviga il sito.
 *
 * Con @ControllerAdvice, invece, intercettiamo le eccezioni a livello di
 * applicazione e decidiamo noi quale pagina mostrare.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Caso specifico: una risorsa (Torneo, Squadra, Partita...) non esiste.
     * Restituiamo una pagina 404 dedicata e leggibile.
     */
    @ExceptionHandler(RisorsaNonTrovataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String gestisciRisorsaNonTrovata(RisorsaNonTrovataException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/404";
    }

    /**
     * Caso generico: qualunque altra eccezione non prevista (bug, errore di
     * accesso al database, ecc.). Mostriamo una pagina di errore generica
     * invece di uno stack trace, ma logghiamo l'eccezione completa in console
     * per poterla debuggare durante lo sviluppo.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String gestisciErroreGenerico(Exception ex, Model model) {
        ex.printStackTrace(); // utile in fase di sviluppo per leggere il problema reale
        model.addAttribute("messaggio", "Si è verificato un errore imprevisto. Riprova più tardi.");
        return "error/generico";
    }
}
