package it.filten.universita.service.impl;

import it.filten.universita.service.StudenteService;
import it.filten.universita.domain.Studente;
import it.filten.universita.repository.StudenteRepository;
import it.filten.universita.service.dto.StudenteDTO;
import it.filten.universita.service.mapper.StudenteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Studente.
 */
@Service
@Transactional
public class StudenteServiceImpl implements StudenteService {

    private final Logger log = LoggerFactory.getLogger(StudenteServiceImpl.class);

    private final StudenteRepository studenteRepository;

    private final StudenteMapper studenteMapper;

    public StudenteServiceImpl(StudenteRepository studenteRepository, StudenteMapper studenteMapper) {
        this.studenteRepository = studenteRepository;
        this.studenteMapper = studenteMapper;
    }

    /**
     * Save a studente.
     *
     * @param studenteDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StudenteDTO save(StudenteDTO studenteDTO) {
        log.debug("Request to save Studente : {}", studenteDTO);
        Studente studente = studenteMapper.toEntity(studenteDTO);
        studente = studenteRepository.save(studente);
        return studenteMapper.toDto(studente);
    }

    /**
     * Get all the studenti.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudenteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Studenti");
        return studenteRepository.findAll(pageable)
            .map(studenteMapper::toDto);
    }

    /**
     * Get all the Studente with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<StudenteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return studenteRepository.findAllWithEagerRelationships(pageable).map(studenteMapper::toDto);
    }
    

    /**
     * Get one studente by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StudenteDTO> findOne(Long id) {
        log.debug("Request to get Studente : {}", id);
        return studenteRepository.findOneWithEagerRelationships(id)
            .map(studenteMapper::toDto);
    }

    /**
     * Delete the studente by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Studente : {}", id);        studenteRepository.deleteById(id);
    }
}
