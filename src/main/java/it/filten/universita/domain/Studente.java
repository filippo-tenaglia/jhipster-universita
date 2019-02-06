package it.filten.universita.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Studente.
 */
@Entity
@Table(name = "studente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Studente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "data_nascita", nullable = false)
    private LocalDate data_nascita;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "cognome", nullable = false)
    private String cognome;

    @NotNull
    @Column(name = "matricola", nullable = false)
    private String matricola;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("studenti")
    private Facolta facolta;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "studente_corsi",
               joinColumns = @JoinColumn(name = "studente_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "corsi_id", referencedColumnName = "id"))
    private Set<Corso> corsi = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData_nascita() {
        return data_nascita;
    }

    public Studente data_nascita(LocalDate data_nascita) {
        this.data_nascita = data_nascita;
        return this;
    }

    public void setData_nascita(LocalDate data_nascita) {
        this.data_nascita = data_nascita;
    }

    public String getNome() {
        return nome;
    }

    public Studente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Studente cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getMatricola() {
        return matricola;
    }

    public Studente matricola(String matricola) {
        this.matricola = matricola;
        return this;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public Facolta getFacolta() {
        return facolta;
    }

    public Studente facolta(Facolta facolta) {
        this.facolta = facolta;
        return this;
    }

    public void setFacolta(Facolta facolta) {
        this.facolta = facolta;
    }

    public Set<Corso> getCorsi() {
        return corsi;
    }

    public Studente corsi(Set<Corso> corsi) {
        this.corsi = corsi;
        return this;
    }

    public Studente addCorsi(Corso corso) {
        this.corsi.add(corso);
        corso.getStudenti().add(this);
        return this;
    }

    public Studente removeCorsi(Corso corso) {
        this.corsi.remove(corso);
        corso.getStudenti().remove(this);
        return this;
    }

    public void setCorsi(Set<Corso> corsi) {
        this.corsi = corsi;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Studente studente = (Studente) o;
        if (studente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), studente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Studente{" +
            "id=" + getId() +
            ", data_nascita='" + getData_nascita() + "'" +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", matricola='" + getMatricola() + "'" +
            "}";
    }
}
