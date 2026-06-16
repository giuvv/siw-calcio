package com.tornei.calcioamatoriale.model;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

@Entity
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il nome della squadra è obbligatorio")
    @Size(max = 100)
    private String nome;

    @NotNull(message = "L'anno di fondazione è obbligatorio")
    @Min(value = 1800, message = "Anno non plausibile")
    @Max(value = 2100)
    private Integer annoFondazione;

    @NotBlank(message = "La città è obbligatoria")
    @Size(max = 100)
    private String citta;

    @OneToMany(mappedBy = "squadra", cascade = CascadeType.ALL)
    private List<Giocatore> giocatori;

    @ManyToMany(mappedBy = "squadre")
    private List<Torneo> tornei;

    public Squadra() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAnnoFondazione() {
        return annoFondazione;
    }

    public void setAnnoFondazione(Integer annoFondazione) {
        this.annoFondazione = annoFondazione;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(List<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    public List<Torneo> getTornei() {
        return tornei;
    }

    public void setTornei(List<Torneo> tornei) {
        this.tornei = tornei;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Squadra other = (Squadra) obj;
        return Objects.equals(id, other.id);
    }
}