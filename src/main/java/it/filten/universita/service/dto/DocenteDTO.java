package it.filten.universita.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Docente entity.
 */
public class DocenteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String cognome;


    private Long facoltaId;

    private String facoltaNome;

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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocenteDTO docenteDTO = (DocenteDTO) o;
        if (docenteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), docenteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocenteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", facolta=" + getFacoltaId() +
            ", facolta='" + getFacoltaNome() + "'" +
            "}";
    }
}
