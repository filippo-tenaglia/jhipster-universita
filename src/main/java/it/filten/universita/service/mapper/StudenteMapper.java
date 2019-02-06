package it.filten.universita.service.mapper;

import it.filten.universita.domain.*;
import it.filten.universita.service.dto.StudenteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Studente and its DTO StudenteDTO.
 */
@Mapper(componentModel = "spring", uses = {FacoltaMapper.class, CorsoMapper.class})
public interface StudenteMapper extends EntityMapper<StudenteDTO, Studente> {

    @Mapping(source = "facolta.id", target = "facoltaId")
    @Mapping(source = "facolta.nome", target = "facoltaNome")
    StudenteDTO toDto(Studente studente);

    @Mapping(source = "facoltaId", target = "facolta")
    Studente toEntity(StudenteDTO studenteDTO);

    default Studente fromId(Long id) {
        if (id == null) {
            return null;
        }
        Studente studente = new Studente();
        studente.setId(id);
        return studente;
    }
}
