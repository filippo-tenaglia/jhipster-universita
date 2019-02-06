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

import it.filten.universita.domain.Studente;
import it.filten.universita.domain.*; // for static metamodels
import it.filten.universita.repository.StudenteRepository;
import it.filten.universita.service.dto.StudenteCriteria;
import it.filten.universita.service.dto.StudenteDTO;
import it.filten.universita.service.mapper.StudenteMapper;

/**
 * Service for executing complex queries for Studente entities in the database.
 * The main input is a {@link StudenteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudenteDTO} or a {@link Page} of {@link StudenteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudenteQueryService extends QueryService<Studente> {

    private final Logger log = LoggerFactory.getLogger(StudenteQueryService.class);

    private final StudenteRepository studenteRepository;

    private final StudenteMapper studenteMapper;

    public StudenteQueryService(StudenteRepository studenteRepository, StudenteMapper studenteMapper) {
        this.studenteRepository = studenteRepository;
        this.studenteMapper = studenteMapper;
    }

    /**
     * Return a {@link List} of {@link StudenteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudenteDTO> findByCriteria(StudenteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Studente> specification = createSpecification(criteria);
        return studenteMapper.toDto(studenteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudenteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudenteDTO> findByCriteria(StudenteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Studente> specification = createSpecification(criteria);
        return studenteRepository.findAll(specification, page)
            .map(studenteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudenteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Studente> specification = createSpecification(criteria);
        return studenteRepository.count(specification);
    }

    /**
     * Function to convert StudenteCriteria to a {@link Specification}
     */
    private Specification<Studente> createSpecification(StudenteCriteria criteria) {
        Specification<Studente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Studente_.id));
            }
            if (criteria.getData_nascita() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData_nascita(), Studente_.data_nascita));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Studente_.nome));
            }
            if (criteria.getCognome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCognome(), Studente_.cognome));
            }
            if (criteria.getMatricola() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatricola(), Studente_.matricola));
            }
            if (criteria.getFacoltaId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacoltaId(),
                    root -> root.join(Studente_.facolta, JoinType.LEFT).get(Facolta_.id)));
            }
            if (criteria.getCorsiId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorsiId(),
                    root -> root.join(Studente_.corsi, JoinType.LEFT).get(Corso_.id)));
            }
        }
        return specification;
    }
}
