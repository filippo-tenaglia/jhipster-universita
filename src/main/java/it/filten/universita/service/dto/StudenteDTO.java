package it.filten.universita.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Studente entity.
 */
public class StudenteDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate data_nascita;

    @NotNull
    private String nome;

    @NotNull
    private String cognome;

    @NotNull
    private String matricola;


    private Long facoltaId;

    private String facoltaNome;

    private Set<CorsoDTO> corsi = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData_nascita() {
        return data_nascita;
    }

    public void setData_nascita(LocalDate data_nascita) {
        this.data_nascita = data_nascita;
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

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
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

    public Set<CorsoDTO> getCorsi() {
        return corsi;
    }

    public void setCorsi(Set<CorsoDTO> corsi) {
        this.corsi = corsi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StudenteDTO studenteDTO = (StudenteDTO) o;
        if (studenteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), studenteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StudenteDTO{" +
            "id=" + getId() +
            ", data_nascita='" + getData_nascita() + "'" +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", matricola='" + getMatricola() + "'" +
            ", facolta=" + getFacoltaId() +
            ", facolta='" + getFacoltaNome() + "'" +
            "}";
    }
}
