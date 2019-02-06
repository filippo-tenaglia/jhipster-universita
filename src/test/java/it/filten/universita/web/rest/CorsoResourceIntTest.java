package it.filten.universita.web.rest;

import it.filten.universita.UniversitaApp;

import it.filten.universita.domain.Corso;
import it.filten.universita.domain.Facolta;
import it.filten.universita.domain.Docente;
import it.filten.universita.domain.Studente;
import it.filten.universita.repository.CorsoRepository;
import it.filten.universita.service.CorsoService;
import it.filten.universita.service.dto.CorsoDTO;
import it.filten.universita.service.mapper.CorsoMapper;
import it.filten.universita.web.rest.errors.ExceptionTranslator;
import it.filten.universita.service.dto.CorsoCriteria;
import it.filten.universita.service.CorsoQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static it.filten.universita.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CorsoResource REST controller.
 *
 * @see CorsoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniversitaApp.class)
public class CorsoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private CorsoRepository corsoRepository;

    @Autowired
    private CorsoMapper corsoMapper;

    @Autowired
    private CorsoService corsoService;

    @Autowired
    private CorsoQueryService corsoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCorsoMockMvc;

    private Corso corso;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorsoResource corsoResource = new CorsoResource(corsoService, corsoQueryService);
        this.restCorsoMockMvc = MockMvcBuilders.standaloneSetup(corsoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corso createEntity(EntityManager em) {
        Corso corso = new Corso()
            .nome(DEFAULT_NOME);
        // Add required entity
        Facolta facolta = FacoltaResourceIntTest.createEntity(em);
        em.persist(facolta);
        em.flush();
        corso.setFacolta(facolta);
        // Add required entity
        Docente docente = DocenteResourceIntTest.createEntity(em);
        em.persist(docente);
        em.flush();
        corso.setDocente(docente);
        return corso;
    }

    @Before
    public void initTest() {
        corso = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorso() throws Exception {
        int databaseSizeBeforeCreate = corsoRepository.findAll().size();

        // Create the Corso
        CorsoDTO corsoDTO = corsoMapper.toDto(corso);
        restCorsoMockMvc.perform(post("/api/corsi")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corsoDTO)))
            .andExpect(status().isCreated());

        // Validate the Corso in the database
        List<Corso> corsoList = corsoRepository.findAll();
        assertThat(corsoList).hasSize(databaseSizeBeforeCreate + 1);
        Corso testCorso = corsoList.get(corsoList.size() - 1);
        assertThat(testCorso.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createCorsoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = corsoRepository.findAll().size();

        // Create the Corso with an existing ID
        corso.setId(1L);
        CorsoDTO corsoDTO = corsoMapper.toDto(corso);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorsoMockMvc.perform(post("/api/corsi")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corsoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Corso in the database
        List<Corso> corsoList = corsoRepository.findAll();
        assertThat(corsoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = corsoRepository.findAll().size();
        // set the field null
        corso.setNome(null);

        // Create the Corso, which fails.
        CorsoDTO corsoDTO = corsoMapper.toDto(corso);

        restCorsoMockMvc.perform(post("/api/corsi")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corsoDTO)))
            .andExpect(status().isBadRequest());

        List<Corso> corsoList = corsoRepository.findAll();
        assertThat(corsoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCorsi() throws Exception {
        // Initialize the database
        corsoRepository.saveAndFlush(corso);

        // Get all the corsoList
        restCorsoMockMvc.perform(get("/api/corsi?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }
    
    @Test
    @Transactional
    public void getCorso() throws Exception {
        // Initialize the database
        corsoRepository.saveAndFlush(corso);

        // Get the corso
        restCorsoMockMvc.perform(get("/api/corsi/{id}", corso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(corso.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllCorsiByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        corsoRepository.saveAndFlush(corso);

        // Get all the corsoList where nome equals to DEFAULT_NOME
        defaultCorsoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the corsoList where nome equals to UPDATED_NOME
        defaultCorsoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCorsiByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        corsoRepository.saveAndFlush(corso);

        // Get all the corsoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultCorsoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the corsoList where nome equals to UPDATED_NOME
        defaultCorsoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCorsiByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        corsoRepository.saveAndFlush(corso);

        // Get all the corsoList where nome is not null
        defaultCorsoShouldBeFound("nome.specified=true");

        // Get all the corsoList where nome is null
        defaultCorsoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllCorsiByFacoltaIsEqualToSomething() throws Exception {
        // Initialize the database
        Facolta facolta = FacoltaResourceIntTest.createEntity(em);
        em.persist(facolta);
        em.flush();
        corso.setFacolta(facolta);
        corsoRepository.saveAndFlush(corso);
        Long facoltaId = facolta.getId();

        // Get all the corsoList where facolta equals to facoltaId
        defaultCorsoShouldBeFound("facoltaId.equals=" + facoltaId);

        // Get all the corsoList where facolta equals to facoltaId + 1
        defaultCorsoShouldNotBeFound("facoltaId.equals=" + (facoltaId + 1));
    }


    @Test
    @Transactional
    public void getAllCorsiByDocenteIsEqualToSomething() throws Exception {
        // Initialize the database
        Docente docente = DocenteResourceIntTest.createEntity(em);
        em.persist(docente);
        em.flush();
        corso.setDocente(docente);
        corsoRepository.saveAndFlush(corso);
        Long docenteId = docente.getId();

        // Get all the corsoList where docente equals to docenteId
        defaultCorsoShouldBeFound("docenteId.equals=" + docenteId);

        // Get all the corsoList where docente equals to docenteId + 1
        defaultCorsoShouldNotBeFound("docenteId.equals=" + (docenteId + 1));
    }


    @Test
    @Transactional
    public void getAllCorsiByStudentiIsEqualToSomething() throws Exception {
        // Initialize the database
        Studente studenti = StudenteResourceIntTest.createEntity(em);
        em.persist(studenti);
        em.flush();
        corso.addStudenti(studenti);
        corsoRepository.saveAndFlush(corso);
        Long studentiId = studenti.getId();

        // Get all the corsoList where studenti equals to studentiId
        defaultCorsoShouldBeFound("studentiId.equals=" + studentiId);

        // Get all the corsoList where studenti equals to studentiId + 1
        defaultCorsoShouldNotBeFound("studentiId.equals=" + (studentiId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCorsoShouldBeFound(String filter) throws Exception {
        restCorsoMockMvc.perform(get("/api/corsi?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));

        // Check, that the count call also returns 1
        restCorsoMockMvc.perform(get("/api/corsi/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCorsoShouldNotBeFound(String filter) throws Exception {
        restCorsoMockMvc.perform(get("/api/corsi?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorsoMockMvc.perform(get("/api/corsi/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCorso() throws Exception {
        // Get the corso
        restCorsoMockMvc.perform(get("/api/corsi/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorso() throws Exception {
        // Initialize the database
        corsoRepository.saveAndFlush(corso);

        int databaseSizeBeforeUpdate = corsoRepository.findAll().size();

        // Update the corso
        Corso updatedCorso = corsoRepository.findById(corso.getId()).get();
        // Disconnect from session so that the updates on updatedCorso are not directly saved in db
        em.detach(updatedCorso);
        updatedCorso
            .nome(UPDATED_NOME);
        CorsoDTO corsoDTO = corsoMapper.toDto(updatedCorso);

        restCorsoMockMvc.perform(put("/api/corsi")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corsoDTO)))
            .andExpect(status().isOk());

        // Validate the Corso in the database
        List<Corso> corsoList = corsoRepository.findAll();
        assertThat(corsoList).hasSize(databaseSizeBeforeUpdate);
        Corso testCorso = corsoList.get(corsoList.size() - 1);
        assertThat(testCorso.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingCorso() throws Exception {
        int databaseSizeBeforeUpdate = corsoRepository.findAll().size();

        // Create the Corso
        CorsoDTO corsoDTO = corsoMapper.toDto(corso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorsoMockMvc.perform(put("/api/corsi")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corsoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Corso in the database
        List<Corso> corsoList = corsoRepository.findAll();
        assertThat(corsoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCorso() throws Exception {
        // Initialize the database
        corsoRepository.saveAndFlush(corso);

        int databaseSizeBeforeDelete = corsoRepository.findAll().size();

        // Delete the corso
        restCorsoMockMvc.perform(delete("/api/corsi/{id}", corso.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Corso> corsoList = corsoRepository.findAll();
        assertThat(corsoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Corso.class);
        Corso corso1 = new Corso();
        corso1.setId(1L);
        Corso corso2 = new Corso();
        corso2.setId(corso1.getId());
        assertThat(corso1).isEqualTo(corso2);
        corso2.setId(2L);
        assertThat(corso1).isNotEqualTo(corso2);
        corso1.setId(null);
        assertThat(corso1).isNotEqualTo(corso2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorsoDTO.class);
        CorsoDTO corsoDTO1 = new CorsoDTO();
        corsoDTO1.setId(1L);
        CorsoDTO corsoDTO2 = new CorsoDTO();
        assertThat(corsoDTO1).isNotEqualTo(corsoDTO2);
        corsoDTO2.setId(corsoDTO1.getId());
        assertThat(corsoDTO1).isEqualTo(corsoDTO2);
        corsoDTO2.setId(2L);
        assertThat(corsoDTO1).isNotEqualTo(corsoDTO2);
        corsoDTO1.setId(null);
        assertThat(corsoDTO1).isNotEqualTo(corsoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(corsoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(corsoMapper.fromId(null)).isNull();
    }
}
