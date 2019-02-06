package it.filten.universita.service;

import it.filten.universita.service.dto.CorsoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Corso.
 */
public interface CorsoService {

    /**
     * Save a corso.
     *
     * @param corsoDTO the entity to save
     * @return the persisted entity
     */
    CorsoDTO save(CorsoDTO corsoDTO);

    /**
     * Get all the corsi.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CorsoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" corso.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CorsoDTO> findOne(Long id);

    /**
     * Delete the "id" corso.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
