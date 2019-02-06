package it.filten.universita.service.impl;

import it.filten.universita.service.DocenteService;
import it.filten.universita.domain.Docente;
import it.filten.universita.repository.DocenteRepository;
import it.filten.universita.service.dto.DocenteDTO;
import it.filten.universita.service.mapper.DocenteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Docente.
 */
@Service
@Transactional
public class DocenteServiceImpl implements DocenteService {

    private final Logger log = LoggerFactory.getLogger(DocenteServiceImpl.class);

    private final DocenteRepository docenteRepository;

    private final DocenteMapper docenteMapper;

    public DocenteServiceImpl(DocenteRepository docenteRepository, DocenteMapper docenteMapper) {
        this.docenteRepository = docenteRepository;
        this.docenteMapper = docenteMapper;
    }

    /**
     * Save a docente.
     *
     * @param docenteDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DocenteDTO save(DocenteDTO docenteDTO) {
        log.debug("Request to save Docente : {}", docenteDTO);
        Docente docente = docenteMapper.toEntity(docenteDTO);
        docente = docenteRepository.save(docente);
        return docenteMapper.toDto(docente);
    }

    /**
     * Get all the docentes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DocenteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Docentes");
        return docenteRepository.findAll(pageable)
            .map(docenteMapper::toDto);
    }


    /**
     * Get one docente by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DocenteDTO> findOne(Long id) {
        log.debug("Request to get Docente : {}", id);
        return docenteRepository.findById(id)
            .map(docenteMapper::toDto);
    }

    /**
     * Delete the docente by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Docente : {}", id);        docenteRepository.deleteById(id);
    }
}
