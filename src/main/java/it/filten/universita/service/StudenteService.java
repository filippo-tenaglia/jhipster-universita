package it.filten.universita.service;

import it.filten.universita.service.dto.StudenteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Studente.
 */
public interface StudenteService {

    /**
     * Save a studente.
     *
     * @param studenteDTO the entity to save
     * @return the persisted entity
     */
    StudenteDTO save(StudenteDTO studenteDTO);

    /**
     * Get all the studenti.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StudenteDTO> findAll(Pageable pageable);

    /**
     * Get all the Studente with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<StudenteDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" studente.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StudenteDTO> findOne(Long id);

    /**
     * Delete the "id" studente.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
