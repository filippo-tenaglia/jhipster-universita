package it.filten.universita.service.mapper;

import it.filten.universita.domain.*;
import it.filten.universita.service.dto.CorsoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Corso and its DTO CorsoDTO.
 */
@Mapper(componentModel = "spring", uses = {FacoltaMapper.class, DocenteMapper.class})
public interface CorsoMapper extends EntityMapper<CorsoDTO, Corso> {

    @Mapping(source = "facolta.id", target = "facoltaId")
    @Mapping(source = "facolta.nome", target = "facoltaNome")
    @Mapping(source = "docente.id", target = "docenteId")
    @Mapping(source = "docente.cognome", target = "docenteCognome")
    CorsoDTO toDto(Corso corso);

    @Mapping(source = "facoltaId", target = "facolta")
    @Mapping(source = "docenteId", target = "docente")
    @Mapping(target = "studenti", ignore = true)
    Corso toEntity(CorsoDTO corsoDTO);

    default Corso fromId(Long id) {
        if (id == null) {
            return null;
        }
        Corso corso = new Corso();
        corso.setId(id);
        return corso;
    }
}
