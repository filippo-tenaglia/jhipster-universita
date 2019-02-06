package it.filten.universita.service;

import it.filten.universita.service.dto.FacoltaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Facolta.
 */
public interface FacoltaService {

    /**
     * Save a facolta.
     *
     * @param facoltaDTO the entity to save
     * @return the persisted entity
     */
    FacoltaDTO save(FacoltaDTO facoltaDTO);

    /**
     * Get all the facoltas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FacoltaDTO> findAll(Pageable pageable);


    /**
     * Get the "id" facolta.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FacoltaDTO> findOne(Long id);

    /**
     * Delete the "id" facolta.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
