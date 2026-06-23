package com.tornei.calcioamatoriale.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Data e ora sono obbligatorie")
    private LocalDateTime dataOra;

    @NotBlank(message = "Il luogo è obbligatorio")
    private String luogo;

    @Min(value = 0, message = "I gol non possono essere negativi")
    private Integer goalsHome;

    @Min(value = 0, message = "I gol non possono essere negativi")
    private Integer goalsAway;
    
    //SCHEDULED/PLAYED
    private String stato;

    @ManyToOne
    private Torneo torneo;

    @ManyToOne
    private Squadra squadraHome;

    @ManyToOne
    private Squadra squadraAway;

    @ManyToOne
    private Arbitro arbitro;

    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Commento> commenti;


    public Partita() {
    }

    //GETTER E SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Integer getGoalsHome() {
        return goalsHome;
    }

    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    public Integer getGoalsAway() {
        return goalsAway;
    }

    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Squadra getSquadraHome() {
        return squadraHome;
    }

    public void setSquadraHome(Squadra squadraHome) {
        this.squadraHome = squadraHome;
    }

    public Squadra getSquadraAway() {
        return squadraAway;
    }

    public void setSquadraAway(Squadra squadraAway) {
        this.squadraAway = squadraAway;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public List<Commento> getCommenti() {
        return commenti;
    }

    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Partita other = (Partita) obj;
        return Objects.equals(id, other.id);
    }
}