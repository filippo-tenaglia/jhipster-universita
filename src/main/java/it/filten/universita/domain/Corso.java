package it.filten.universita.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Corso.
 */
@Entity
@Table(name = "corso")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Corso extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("corsi")
    private Facolta facolta;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("corsi")
    private Docente docente;

    @ManyToMany(mappedBy = "corsi")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Studente> studenti = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Corso nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Facolta getFacolta() {
        return facolta;
    }

    public Corso facolta(Facolta facolta) {
        this.facolta = facolta;
        return this;
    }

    public void setFacolta(Facolta facolta) {
        this.facolta = facolta;
    }

    public Docente getDocente() {
        return docente;
    }

    public Corso docente(Docente docente) {
        this.docente = docente;
        return this;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Set<Studente> getStudenti() {
        return studenti;
    }

    public Corso studenti(Set<Studente> studenti) {
        this.studenti = studenti;
        return this;
    }

    public Corso addStudenti(Studente studente) {
        this.studenti.add(studente);
        studente.getCorsi().add(this);
        return this;
    }

    public Corso removeStudenti(Studente studente) {
        this.studenti.remove(studente);
        studente.getCorsi().remove(this);
        return this;
    }

    public void setStudenti(Set<Studente> studenti) {
        this.studenti = studenti;
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
        Corso corso = (Corso) o;
        if (corso.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), corso.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Corso{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
