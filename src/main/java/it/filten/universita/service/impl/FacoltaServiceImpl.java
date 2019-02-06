package it.filten.universita.service.impl;

import it.filten.universita.service.FacoltaService;
import it.filten.universita.domain.Facolta;
import it.filten.universita.repository.FacoltaRepository;
import it.filten.universita.service.dto.FacoltaDTO;
import it.filten.universita.service.mapper.FacoltaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Facolta.
 */
@Service
@Transactional
public class FacoltaServiceImpl implements FacoltaService {

    private final Logger log = LoggerFactory.getLogger(FacoltaServiceImpl.class);

    private final FacoltaRepository facoltaRepository;

    private final FacoltaMapper facoltaMapper;

    public FacoltaServiceImpl(FacoltaRepository facoltaRepository, FacoltaMapper facoltaMapper) {
        this.facoltaRepository = facoltaRepository;
        this.facoltaMapper = facoltaMapper;
    }

    /**
     * Save a facolta.
     *
     * @param facoltaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FacoltaDTO save(FacoltaDTO facoltaDTO) {
        log.debug("Request to save Facolta : {}", facoltaDTO);
        Facolta facolta = facoltaMapper.toEntity(facoltaDTO);
        facolta = facoltaRepository.save(facolta);
        return facoltaMapper.toDto(facolta);
    }

    /**
     * Get all the facoltas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FacoltaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facoltas");
        return facoltaRepository.findAll(pageable)
            .map(facoltaMapper::toDto);
    }


    /**
     * Get one facolta by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FacoltaDTO> findOne(Long id) {
        log.debug("Request to get Facolta : {}", id);
        return facoltaRepository.findById(id)
            .map(facoltaMapper::toDto);
    }

    /**
     * Delete the facolta by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facolta : {}", id);        facoltaRepository.deleteById(id);
    }
}
