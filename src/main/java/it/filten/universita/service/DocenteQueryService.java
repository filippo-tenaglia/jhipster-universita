package it.filten.universita.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import it.filten.universita.domain.Docente;
import it.filten.universita.domain.*; // for static metamodels
import it.filten.universita.repository.DocenteRepository;
import it.filten.universita.service.dto.DocenteCriteria;
import it.filten.universita.service.dto.DocenteDTO;
import it.filten.universita.service.mapper.DocenteMapper;

/**
 * Service for executing complex queries for Docente entities in the database.
 * The main input is a {@link DocenteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocenteDTO} or a {@link Page} of {@link DocenteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocenteQueryService extends QueryService<Docente> {

    private final Logger log = LoggerFactory.getLogger(DocenteQueryService.class);

    private final DocenteRepository docenteRepository;

    private final DocenteMapper docenteMapper;

    public DocenteQueryService(DocenteRepository docenteRepository, DocenteMapper docenteMapper) {
        this.docenteRepository = docenteRepository;
        this.docenteMapper = docenteMapper;
    }

    /**
     * Return a {@link List} of {@link DocenteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocenteDTO> findByCriteria(DocenteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Docente> specification = createSpecification(criteria);
        return docenteMapper.toDto(docenteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocenteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocenteDTO> findByCriteria(DocenteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Docente> specification = createSpecification(criteria);
        return docenteRepository.findAll(specification, page)
            .map(docenteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocenteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Docente> specification = createSpecification(criteria);
        return docenteRepository.count(specification);
    }

    /**
     * Function to convert DocenteCriteria to a {@link Specification}
     */
    private Specification<Docente> createSpecification(DocenteCriteria criteria) {
        Specification<Docente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Docente_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Docente_.nome));
            }
            if (criteria.getCognome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCognome(), Docente_.cognome));
            }
            if (criteria.getFacoltaId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacoltaId(),
                    root -> root.join(Docente_.facolta, JoinType.LEFT).get(Facolta_.id)));
            }
        }
        return specification;
    }
}
