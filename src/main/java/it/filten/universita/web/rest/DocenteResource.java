package it.filten.universita.web.rest;
import it.filten.universita.service.DocenteService;
import it.filten.universita.web.rest.errors.BadRequestAlertException;
import it.filten.universita.web.rest.util.HeaderUtil;
import it.filten.universita.web.rest.util.PaginationUtil;
import it.filten.universita.service.dto.DocenteDTO;
import it.filten.universita.service.dto.DocenteCriteria;
import it.filten.universita.service.DocenteQueryService;
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
 * REST controller for managing Docente.
 */
@RestController
@RequestMapping("/api")
public class DocenteResource {

    private final Logger log = LoggerFactory.getLogger(DocenteResource.class);

    private static final String ENTITY_NAME = "docente";

    private final DocenteService docenteService;

    private final DocenteQueryService docenteQueryService;

    public DocenteResource(DocenteService docenteService, DocenteQueryService docenteQueryService) {
        this.docenteService = docenteService;
        this.docenteQueryService = docenteQueryService;
    }

    /**
     * POST  /docentes : Create a new docente.
     *
     * @param docenteDTO the docenteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new docenteDTO, or with status 400 (Bad Request) if the docente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/docentes")
    public ResponseEntity<DocenteDTO> createDocente(@Valid @RequestBody DocenteDTO docenteDTO) throws URISyntaxException {
        log.debug("REST request to save Docente : {}", docenteDTO);
        if (docenteDTO.getId() != null) {
            throw new BadRequestAlertException("A new docente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocenteDTO result = docenteService.save(docenteDTO);
        return ResponseEntity.created(new URI("/api/docentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /docentes : Updates an existing docente.
     *
     * @param docenteDTO the docenteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated docenteDTO,
     * or with status 400 (Bad Request) if the docenteDTO is not valid,
     * or with status 500 (Internal Server Error) if the docenteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/docentes")
    public ResponseEntity<DocenteDTO> updateDocente(@Valid @RequestBody DocenteDTO docenteDTO) throws URISyntaxException {
        log.debug("REST request to update Docente : {}", docenteDTO);
        if (docenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DocenteDTO result = docenteService.save(docenteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, docenteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /docentes : get all the docentes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of docentes in body
     */
    @GetMapping("/docentes")
    public ResponseEntity<List<DocenteDTO>> getAllDocentes(DocenteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Docentes by criteria: {}", criteria);
        Page<DocenteDTO> page = docenteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/docentes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /docentes/count : count all the docentes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/docentes/count")
    public ResponseEntity<Long> countDocentes(DocenteCriteria criteria) {
        log.debug("REST request to count Docentes by criteria: {}", criteria);
        return ResponseEntity.ok().body(docenteQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /docentes/:id : get the "id" docente.
     *
     * @param id the id of the docenteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the docenteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/docentes/{id}")
    public ResponseEntity<DocenteDTO> getDocente(@PathVariable Long id) {
        log.debug("REST request to get Docente : {}", id);
        Optional<DocenteDTO> docenteDTO = docenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docenteDTO);
    }

    /**
     * DELETE  /docentes/:id : delete the "id" docente.
     *
     * @param id the id of the docenteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/docentes/{id}")
    public ResponseEntity<Void> deleteDocente(@PathVariable Long id) {
        log.debug("REST request to delete Docente : {}", id);
        docenteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
