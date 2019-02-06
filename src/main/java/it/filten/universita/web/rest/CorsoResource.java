package it.filten.universita.web.rest;
import it.filten.universita.service.CorsoService;
import it.filten.universita.web.rest.errors.BadRequestAlertException;
import it.filten.universita.web.rest.util.HeaderUtil;
import it.filten.universita.web.rest.util.PaginationUtil;
import it.filten.universita.service.dto.CorsoDTO;
import it.filten.universita.service.dto.CorsoCriteria;
import it.filten.universita.service.CorsoQueryService;
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
 * REST controller for managing Corso.
 */
@RestController
@RequestMapping("/api")
public class CorsoResource {

    private final Logger log = LoggerFactory.getLogger(CorsoResource.class);

    private static final String ENTITY_NAME = "corso";

    private final CorsoService corsoService;

    private final CorsoQueryService corsoQueryService;

    public CorsoResource(CorsoService corsoService, CorsoQueryService corsoQueryService) {
        this.corsoService = corsoService;
        this.corsoQueryService = corsoQueryService;
    }

    /**
     * POST  /corsi : Create a new corso.
     *
     * @param corsoDTO the corsoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new corsoDTO, or with status 400 (Bad Request) if the corso has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/corsi")
    public ResponseEntity<CorsoDTO> createCorso(@Valid @RequestBody CorsoDTO corsoDTO) throws URISyntaxException {
        log.debug("REST request to save Corso : {}", corsoDTO);
        if (corsoDTO.getId() != null) {
            throw new BadRequestAlertException("A new corso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorsoDTO result = corsoService.save(corsoDTO);
        return ResponseEntity.created(new URI("/api/corsi/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /corsi : Updates an existing corso.
     *
     * @param corsoDTO the corsoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated corsoDTO,
     * or with status 400 (Bad Request) if the corsoDTO is not valid,
     * or with status 500 (Internal Server Error) if the corsoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/corsi")
    public ResponseEntity<CorsoDTO> updateCorso(@Valid @RequestBody CorsoDTO corsoDTO) throws URISyntaxException {
        log.debug("REST request to update Corso : {}", corsoDTO);
        if (corsoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CorsoDTO result = corsoService.save(corsoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, corsoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /corsi : get all the corsi.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of corsi in body
     */
    @GetMapping("/corsi")
    public ResponseEntity<List<CorsoDTO>> getAllCorsi(CorsoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Corsi by criteria: {}", criteria);
        Page<CorsoDTO> page = corsoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/corsi");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /corsi/count : count all the corsi.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/corsi/count")
    public ResponseEntity<Long> countCorsi(CorsoCriteria criteria) {
        log.debug("REST request to count Corsi by criteria: {}", criteria);
        return ResponseEntity.ok().body(corsoQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /corsi/:id : get the "id" corso.
     *
     * @param id the id of the corsoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corsoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/corsi/{id}")
    public ResponseEntity<CorsoDTO> getCorso(@PathVariable Long id) {
        log.debug("REST request to get Corso : {}", id);
        Optional<CorsoDTO> corsoDTO = corsoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(corsoDTO);
    }

    /**
     * DELETE  /corsi/:id : delete the "id" corso.
     *
     * @param id the id of the corsoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/corsi/{id}")
    public ResponseEntity<Void> deleteCorso(@PathVariable Long id) {
        log.debug("REST request to delete Corso : {}", id);
        corsoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
