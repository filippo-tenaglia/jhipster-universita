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

import it.filten.universita.domain.Facolta;
import it.filten.universita.domain.*; // for static metamodels
import it.filten.universita.repository.FacoltaRepository;
import it.filten.universita.service.dto.FacoltaCriteria;
import it.filten.universita.service.dto.FacoltaDTO;
import it.filten.universita.service.mapper.FacoltaMapper;

/**
 * Service for executing complex queries for Facolta entities in the database.
 * The main input is a {@link FacoltaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacoltaDTO} or a {@link Page} of {@link FacoltaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacoltaQueryService extends QueryService<Facolta> {

    private final Logger log = LoggerFactory.getLogger(FacoltaQueryService.class);

    private final FacoltaRepository facoltaRepository;

    private final FacoltaMapper facoltaMapper;

    public FacoltaQueryService(FacoltaRepository facoltaRepository, FacoltaMapper facoltaMapper) {
        this.facoltaRepository = facoltaRepository;
        this.facoltaMapper = facoltaMapper;
    }

    /**
     * Return a {@link List} of {@link FacoltaDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacoltaDTO> findByCriteria(FacoltaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
		System.out.println("NomeFilter: "+criteria.getNome().getContains());
        final Specification<Facolta> specification = createSpecification(criteria);
        return facoltaMapper.toDto(facoltaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacoltaDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacoltaDTO> findByCriteria(FacoltaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facolta> specification = createSpecification(criteria);
        return facoltaRepository.findAll(specification, page)
            .map(facoltaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacoltaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Facolta> specification = createSpecification(criteria);
        return facoltaRepository.count(specification);
    }

    /**
     * Function to convert FacoltaCriteria to a {@link Specification}
     */
    private Specification<Facolta> createSpecification(FacoltaCriteria criteria) {
        Specification<Facolta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Facolta_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Facolta_.nome));
            }
        }
        return specification;
    }
}
