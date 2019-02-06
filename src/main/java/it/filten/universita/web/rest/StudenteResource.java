package it.filten.universita.web.rest;
import it.filten.universita.service.StudenteService;
import it.filten.universita.web.rest.errors.BadRequestAlertException;
import it.filten.universita.web.rest.util.HeaderUtil;
import it.filten.universita.web.rest.util.PaginationUtil;
import it.filten.universita.service.dto.StudenteDTO;
import it.filten.universita.service.dto.StudenteCriteria;
import it.filten.universita.service.StudenteQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Studente.
 */
@RestController
@RequestMapping("/api")
public class StudenteResource {

    private final Logger log = LoggerFactory.getLogger(StudenteResource.class);

    private static final String ENTITY_NAME = "studente";

    private final StudenteService studenteService;

    private final StudenteQueryService studenteQueryService;

    public StudenteResource(StudenteService studenteService, StudenteQueryService studenteQueryService) {
        this.studenteService = studenteService;
        this.studenteQueryService = studenteQueryService;
    }

    /**
     * POST  /studenti : Create a new studente.
     *
     * @param studenteDTO the studenteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studenteDTO, or with status 400 (Bad Request) if the studente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/studenti")
    public ResponseEntity<StudenteDTO> createStudente(@Valid @RequestBody StudenteDTO studenteDTO) throws URISyntaxException {
        log.debug("REST request to save Studente : {}", studenteDTO);
        if (studenteDTO.getId() != null) {
            throw new BadRequestAlertException("A new studente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudenteDTO result = studenteService.save(studenteDTO);
        return ResponseEntity.created(new URI("/api/studenti/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /studenti : Updates an existing studente.
     *
     * @param studenteDTO the studenteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studenteDTO,
     * or with status 400 (Bad Request) if the studenteDTO is not valid,
     * or with status 500 (Internal Server Error) if the studenteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/studenti")
    public ResponseEntity<StudenteDTO> updateStudente(@Valid @RequestBody StudenteDTO studenteDTO) throws URISyntaxException {
        log.debug("REST request to update Studente : {}", studenteDTO);
        if (studenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudenteDTO result = studenteService.save(studenteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studenteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /studenti : get all the studenti.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of studenti in body
     */
    @GetMapping("/studenti")
    public ResponseEntity<List<StudenteDTO>> getAllStudenti(StudenteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Studenti by criteria: {}", criteria);
        Page<StudenteDTO> page = studenteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/studenti");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /studenti/count : count all the studenti.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/studenti/count")
    public ResponseEntity<Long> countStudenti(StudenteCriteria criteria) {
        log.debug("REST request to count Studenti by criteria: {}", criteria);
        return ResponseEntity.ok().body(studenteQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /studenti/:id : get the "id" studente.
     *
     * @param id the id of the studenteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studenteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/studenti/{id}")
    public ResponseEntity<StudenteDTO> getStudente(@PathVariable Long id) {
        log.debug("REST request to get Studente : {}", id);
        Optional<StudenteDTO> studenteDTO = studenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studenteDTO);
    }

    /**
     * DELETE  /studenti/:id : delete the "id" studente.
     *
     * @param id the id of the studenteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/studenti/{id}")
    public ResponseEntity<Void> deleteStudente(@PathVariable Long id) {
        log.debug("REST request to delete Studente : {}", id);
        studenteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
