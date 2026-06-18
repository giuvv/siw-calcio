package com.tornei.calcioamatoriale.exception;

/**
 * Lanciata quando si cerca un'entità (Torneo, Squadra, Partita, ...) con un id
 * che non esiste nel database. Viene intercettata da GlobalExceptionHandler
 * e tradotta in una pagina 404 leggibile, invece della Whitelabel Error Page.
 */
public class RisorsaNonTrovataException extends RuntimeException {

    public RisorsaNonTrovataException(String messaggio) {
        super(messaggio);
    }
}
