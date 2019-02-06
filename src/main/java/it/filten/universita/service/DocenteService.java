package it.filten.universita.service;

import it.filten.universita.service.dto.DocenteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Docente.
 */
public interface DocenteService {

    /**
     * Save a docente.
     *
     * @param docenteDTO the entity to save
     * @return the persisted entity
     */
    DocenteDTO save(DocenteDTO docenteDTO);

    /**
     * Get all the docentes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DocenteDTO> findAll(Pageable pageable);


    /**
     * Get the "id" docente.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DocenteDTO> findOne(Long id);

    /**
     * Delete the "id" docente.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
