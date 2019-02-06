package it.filten.universita.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Studente entity. This class is used in StudenteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /studenti?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudenteCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter data_nascita;

    private StringFilter nome;

    private StringFilter cognome;

    private StringFilter matricola;

    private LongFilter facoltaId;

    private LongFilter corsiId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getData_nascita() {
        return data_nascita;
    }

    public void setData_nascita(LocalDateFilter data_nascita) {
        this.data_nascita = data_nascita;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCognome() {
        return cognome;
    }

    public void setCognome(StringFilter cognome) {
        this.cognome = cognome;
    }

    public StringFilter getMatricola() {
        return matricola;
    }

    public void setMatricola(StringFilter matricola) {
        this.matricola = matricola;
    }

    public LongFilter getFacoltaId() {
        return facoltaId;
    }

    public void setFacoltaId(LongFilter facoltaId) {
        this.facoltaId = facoltaId;
    }

    public LongFilter getCorsiId() {
        return corsiId;
    }

    public void setCorsiId(LongFilter corsiId) {
        this.corsiId = corsiId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudenteCriteria that = (StudenteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(data_nascita, that.data_nascita) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cognome, that.cognome) &&
            Objects.equals(matricola, that.matricola) &&
            Objects.equals(facoltaId, that.facoltaId) &&
            Objects.equals(corsiId, that.corsiId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        data_nascita,
        nome,
        cognome,
        matricola,
        facoltaId,
        corsiId
        );
    }

    @Override
    public String toString() {
        return "StudenteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (data_nascita != null ? "data_nascita=" + data_nascita + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (cognome != null ? "cognome=" + cognome + ", " : "") +
                (matricola != null ? "matricola=" + matricola + ", " : "") +
                (facoltaId != null ? "facoltaId=" + facoltaId + ", " : "") +
                (corsiId != null ? "corsiId=" + corsiId + ", " : "") +
            "}";
    }

}
