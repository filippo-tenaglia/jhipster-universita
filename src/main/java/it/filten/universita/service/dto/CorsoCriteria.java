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

/**
 * Criteria class for the Corso entity. This class is used in CorsoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /corsi?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CorsoCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private LongFilter facoltaId;

    private LongFilter docenteId;

    private LongFilter studentiId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public LongFilter getFacoltaId() {
        return facoltaId;
    }

    public void setFacoltaId(LongFilter facoltaId) {
        this.facoltaId = facoltaId;
    }

    public LongFilter getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(LongFilter docenteId) {
        this.docenteId = docenteId;
    }

    public LongFilter getStudentiId() {
        return studentiId;
    }

    public void setStudentiId(LongFilter studentiId) {
        this.studentiId = studentiId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CorsoCriteria that = (CorsoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(facoltaId, that.facoltaId) &&
            Objects.equals(docenteId, that.docenteId) &&
            Objects.equals(studentiId, that.studentiId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        facoltaId,
        docenteId,
        studentiId
        );
    }

    @Override
    public String toString() {
        return "CorsoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (facoltaId != null ? "facoltaId=" + facoltaId + ", " : "") +
                (docenteId != null ? "docenteId=" + docenteId + ", " : "") +
                (studentiId != null ? "studentiId=" + studentiId + ", " : "") +
            "}";
    }

}
