package it.filten.universita.service.mapper;

import it.filten.universita.domain.*;
import it.filten.universita.service.dto.DocenteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Docente and its DTO DocenteDTO.
 */
@Mapper(componentModel = "spring", uses = {FacoltaMapper.class})
public interface DocenteMapper extends EntityMapper<DocenteDTO, Docente> {

    @Mapping(source = "facolta.id", target = "facoltaId")
    @Mapping(source = "facolta.nome", target = "facoltaNome")
    DocenteDTO toDto(Docente docente);

    @Mapping(source = "facoltaId", target = "facolta")
    Docente toEntity(DocenteDTO docenteDTO);

    default Docente fromId(Long id) {
        if (id == null) {
            return null;
        }
        Docente docente = new Docente();
        docente.setId(id);
        return docente;
    }
}
