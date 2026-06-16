package com.tornei.calcioamatoriale.model;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

@Entity
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il nome del torneo è obbligatorio")
    @Size(max = 100, message = "Il nome non può superare 100 caratteri")
    private String nome;

    @NotNull(message = "L'anno è obbligatorio")
    @Min(value = 1900, message = "L'anno deve essere almeno 1900")
    @Max(value = 2100, message = "L'anno non può superare 2100")
    private Integer anno;

    @Size(max = 500, message = "La descrizione non può superare 500 caratteri")
    private String descrizione;

    @ManyToMany
    private List<Squadra> squadre;

    public Torneo() {
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

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<Squadra> getSquadre() {
        return squadre;
    }

    public void setSquadre(List<Squadra> squadre) {
        this.squadre = squadre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Torneo other = (Torneo) obj;
        return Objects.equals(id, other.id);
    }
   
}