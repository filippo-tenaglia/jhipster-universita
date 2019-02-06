package it.filten.universita.service.impl;

import it.filten.universita.service.CorsoService;
import it.filten.universita.domain.Corso;
import it.filten.universita.repository.CorsoRepository;
import it.filten.universita.service.dto.CorsoDTO;
import it.filten.universita.service.mapper.CorsoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Corso.
 */
@Service
@Transactional
public class CorsoServiceImpl implements CorsoService {

    private final Logger log = LoggerFactory.getLogger(CorsoServiceImpl.class);

    private final CorsoRepository corsoRepository;

    private final CorsoMapper corsoMapper;

    public CorsoServiceImpl(CorsoRepository corsoRepository, CorsoMapper corsoMapper) {
        this.corsoRepository = corsoRepository;
        this.corsoMapper = corsoMapper;
    }

    /**
     * Save a corso.
     *
     * @param corsoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CorsoDTO save(CorsoDTO corsoDTO) {
        log.debug("Request to save Corso : {}", corsoDTO);
        Corso corso = corsoMapper.toEntity(corsoDTO);
        corso = corsoRepository.save(corso);
        return corsoMapper.toDto(corso);
    }

    /**
     * Get all the corsi.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CorsoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Corsi");
        return corsoRepository.findAll(pageable)
            .map(corsoMapper::toDto);
    }


    /**
     * Get one corso by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CorsoDTO> findOne(Long id) {
        log.debug("Request to get Corso : {}", id);
        return corsoRepository.findById(id)
            .map(corsoMapper::toDto);
    }

    /**
     * Delete the corso by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Corso : {}", id);        corsoRepository.deleteById(id);
    }
}
