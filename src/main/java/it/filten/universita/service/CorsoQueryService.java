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

import it.filten.universita.domain.Corso;
import it.filten.universita.domain.*; // for static metamodels
import it.filten.universita.repository.CorsoRepository;
import it.filten.universita.service.dto.CorsoCriteria;
import it.filten.universita.service.dto.CorsoDTO;
import it.filten.universita.service.mapper.CorsoMapper;

/**
 * Service for executing complex queries for Corso entities in the database.
 * The main input is a {@link CorsoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CorsoDTO} or a {@link Page} of {@link CorsoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorsoQueryService extends QueryService<Corso> {

    private final Logger log = LoggerFactory.getLogger(CorsoQueryService.class);

    private final CorsoRepository corsoRepository;

    private final CorsoMapper corsoMapper;

    public CorsoQueryService(CorsoRepository corsoRepository, CorsoMapper corsoMapper) {
        this.corsoRepository = corsoRepository;
        this.corsoMapper = corsoMapper;
    }

    /**
     * Return a {@link List} of {@link CorsoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CorsoDTO> findByCriteria(CorsoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Corso> specification = createSpecification(criteria);
        return corsoMapper.toDto(corsoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CorsoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorsoDTO> findByCriteria(CorsoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Corso> specification = createSpecification(criteria);
        return corsoRepository.findAll(specification, page)
            .map(corsoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorsoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Corso> specification = createSpecification(criteria);
        return corsoRepository.count(specification);
    }

    /**
     * Function to convert CorsoCriteria to a {@link Specification}
     */
    private Specification<Corso> createSpecification(CorsoCriteria criteria) {
        Specification<Corso> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Corso_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Corso_.nome));
            }
            if (criteria.getFacoltaId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacoltaId(),
                    root -> root.join(Corso_.facolta, JoinType.LEFT).get(Facolta_.id)));
            }
            if (criteria.getDocenteId() != null) {
                specification = specification.and(buildSpecification(criteria.getDocenteId(),
                    root -> root.join(Corso_.docente, JoinType.LEFT).get(Docente_.id)));
            }
            if (criteria.getStudentiId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentiId(),
                    root -> root.join(Corso_.studenti, JoinType.LEFT).get(Studente_.id)));
            }
        }
        return specification;
    }
}
