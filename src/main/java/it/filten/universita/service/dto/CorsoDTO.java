package it.filten.universita.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Corso entity.
 */
public class CorsoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;


    private Long facoltaId;

    private String facoltaNome;

    private Long docenteId;

    private String docenteCognome;

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

    public Long getFacoltaId() {
        return facoltaId;
    }

    public void setFacoltaId(Long facoltaId) {
        this.facoltaId = facoltaId;
    }

    public String getFacoltaNome() {
        return facoltaNome;
    }

    public void setFacoltaNome(String facoltaNome) {
        this.facoltaNome = facoltaNome;
    }

    public Long getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Long docenteId) {
        this.docenteId = docenteId;
    }

    public String getDocenteCognome() {
        return docenteCognome;
    }

    public void setDocenteCognome(String docenteCognome) {
        this.docenteCognome = docenteCognome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CorsoDTO corsoDTO = (CorsoDTO) o;
        if (corsoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), corsoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CorsoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", facolta=" + getFacoltaId() +
            ", facolta='" + getFacoltaNome() + "'" +
            ", docente=" + getDocenteId() +
            ", docente='" + getDocenteCognome() + "'" +
            "}";
    }
}
