package it.filten.universita.service.mapper;

import it.filten.universita.domain.*;
import it.filten.universita.service.dto.FacoltaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Facolta and its DTO FacoltaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacoltaMapper extends EntityMapper<FacoltaDTO, Facolta> {



    default Facolta fromId(Long id) {
        if (id == null) {
            return null;
        }
        Facolta facolta = new Facolta();
        facolta.setId(id);
        return facolta;
    }
}
