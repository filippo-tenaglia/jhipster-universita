package it.filten.universita.web.rest;

import it.filten.universita.UniversitaApp;

import it.filten.universita.domain.Docente;
import it.filten.universita.domain.Facolta;
import it.filten.universita.repository.DocenteRepository;
import it.filten.universita.service.DocenteService;
import it.filten.universita.service.dto.DocenteDTO;
import it.filten.universita.service.mapper.DocenteMapper;
import it.filten.universita.web.rest.errors.ExceptionTranslator;
import it.filten.universita.service.dto.DocenteCriteria;
import it.filten.universita.service.DocenteQueryService;

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
 * Test class for the DocenteResource REST controller.
 *
 * @see DocenteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniversitaApp.class)
public class DocenteResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COGNOME = "AAAAAAAAAA";
    private static final String UPDATED_COGNOME = "BBBBBBBBBB";

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DocenteMapper docenteMapper;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private DocenteQueryService docenteQueryService;

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

    private MockMvc restDocenteMockMvc;

    private Docente docente;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocenteResource docenteResource = new DocenteResource(docenteService, docenteQueryService);
        this.restDocenteMockMvc = MockMvcBuilders.standaloneSetup(docenteResource)
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
    public static Docente createEntity(EntityManager em) {
        Docente docente = new Docente()
            .nome(DEFAULT_NOME)
            .cognome(DEFAULT_COGNOME);
        // Add required entity
        Facolta facolta = FacoltaResourceIntTest.createEntity(em);
        em.persist(facolta);
        em.flush();
        docente.setFacolta(facolta);
        return docente;
    }

    @Before
    public void initTest() {
        docente = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocente() throws Exception {
        int databaseSizeBeforeCreate = docenteRepository.findAll().size();

        // Create the Docente
        DocenteDTO docenteDTO = docenteMapper.toDto(docente);
        restDocenteMockMvc.perform(post("/api/docentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docenteDTO)))
            .andExpect(status().isCreated());

        // Validate the Docente in the database
        List<Docente> docenteList = docenteRepository.findAll();
        assertThat(docenteList).hasSize(databaseSizeBeforeCreate + 1);
        Docente testDocente = docenteList.get(docenteList.size() - 1);
        assertThat(testDocente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDocente.getCognome()).isEqualTo(DEFAULT_COGNOME);
    }

    @Test
    @Transactional
    public void createDocenteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = docenteRepository.findAll().size();

        // Create the Docente with an existing ID
        docente.setId(1L);
        DocenteDTO docenteDTO = docenteMapper.toDto(docente);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocenteMockMvc.perform(post("/api/docentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Docente in the database
        List<Docente> docenteList = docenteRepository.findAll();
        assertThat(docenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = docenteRepository.findAll().size();
        // set the field null
        docente.setNome(null);

        // Create the Docente, which fails.
        DocenteDTO docenteDTO = docenteMapper.toDto(docente);

        restDocenteMockMvc.perform(post("/api/docentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docenteDTO)))
            .andExpect(status().isBadRequest());

        List<Docente> docenteList = docenteRepository.findAll();
        assertThat(docenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCognomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = docenteRepository.findAll().size();
        // set the field null
        docente.setCognome(null);

        // Create the Docente, which fails.
        DocenteDTO docenteDTO = docenteMapper.toDto(docente);

        restDocenteMockMvc.perform(post("/api/docentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docenteDTO)))
            .andExpect(status().isBadRequest());

        List<Docente> docenteList = docenteRepository.findAll();
        assertThat(docenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocentes() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get all the docenteList
        restDocenteMockMvc.perform(get("/api/docentes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME.toString())));
    }
    
    @Test
    @Transactional
    public void getDocente() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get the docente
        restDocenteMockMvc.perform(get("/api/docentes/{id}", docente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(docente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME.toString()));
    }

    @Test
    @Transactional
    public void getAllDocentesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get all the docenteList where nome equals to DEFAULT_NOME
        defaultDocenteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the docenteList where nome equals to UPDATED_NOME
        defaultDocenteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDocentesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get all the docenteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultDocenteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the docenteList where nome equals to UPDATED_NOME
        defaultDocenteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDocentesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get all the docenteList where nome is not null
        defaultDocenteShouldBeFound("nome.specified=true");

        // Get all the docenteList where nome is null
        defaultDocenteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocentesByCognomeIsEqualToSomething() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get all the docenteList where cognome equals to DEFAULT_COGNOME
        defaultDocenteShouldBeFound("cognome.equals=" + DEFAULT_COGNOME);

        // Get all the docenteList where cognome equals to UPDATED_COGNOME
        defaultDocenteShouldNotBeFound("cognome.equals=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    public void getAllDocentesByCognomeIsInShouldWork() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get all the docenteList where cognome in DEFAULT_COGNOME or UPDATED_COGNOME
        defaultDocenteShouldBeFound("cognome.in=" + DEFAULT_COGNOME + "," + UPDATED_COGNOME);

        // Get all the docenteList where cognome equals to UPDATED_COGNOME
        defaultDocenteShouldNotBeFound("cognome.in=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    public void getAllDocentesByCognomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        // Get all the docenteList where cognome is not null
        defaultDocenteShouldBeFound("cognome.specified=true");

        // Get all the docenteList where cognome is null
        defaultDocenteShouldNotBeFound("cognome.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocentesByFacoltaIsEqualToSomething() throws Exception {
        // Initialize the database
        Facolta facolta = FacoltaResourceIntTest.createEntity(em);
        em.persist(facolta);
        em.flush();
        docente.setFacolta(facolta);
        docenteRepository.saveAndFlush(docente);
        Long facoltaId = facolta.getId();

        // Get all the docenteList where facolta equals to facoltaId
        defaultDocenteShouldBeFound("facoltaId.equals=" + facoltaId);

        // Get all the docenteList where facolta equals to facoltaId + 1
        defaultDocenteShouldNotBeFound("facoltaId.equals=" + (facoltaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDocenteShouldBeFound(String filter) throws Exception {
        restDocenteMockMvc.perform(get("/api/docentes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)));

        // Check, that the count call also returns 1
        restDocenteMockMvc.perform(get("/api/docentes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDocenteShouldNotBeFound(String filter) throws Exception {
        restDocenteMockMvc.perform(get("/api/docentes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocenteMockMvc.perform(get("/api/docentes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDocente() throws Exception {
        // Get the docente
        restDocenteMockMvc.perform(get("/api/docentes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocente() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        int databaseSizeBeforeUpdate = docenteRepository.findAll().size();

        // Update the docente
        Docente updatedDocente = docenteRepository.findById(docente.getId()).get();
        // Disconnect from session so that the updates on updatedDocente are not directly saved in db
        em.detach(updatedDocente);
        updatedDocente
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME);
        DocenteDTO docenteDTO = docenteMapper.toDto(updatedDocente);

        restDocenteMockMvc.perform(put("/api/docentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docenteDTO)))
            .andExpect(status().isOk());

        // Validate the Docente in the database
        List<Docente> docenteList = docenteRepository.findAll();
        assertThat(docenteList).hasSize(databaseSizeBeforeUpdate);
        Docente testDocente = docenteList.get(docenteList.size() - 1);
        assertThat(testDocente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDocente.getCognome()).isEqualTo(UPDATED_COGNOME);
    }

    @Test
    @Transactional
    public void updateNonExistingDocente() throws Exception {
        int databaseSizeBeforeUpdate = docenteRepository.findAll().size();

        // Create the Docente
        DocenteDTO docenteDTO = docenteMapper.toDto(docente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocenteMockMvc.perform(put("/api/docentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Docente in the database
        List<Docente> docenteList = docenteRepository.findAll();
        assertThat(docenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocente() throws Exception {
        // Initialize the database
        docenteRepository.saveAndFlush(docente);

        int databaseSizeBeforeDelete = docenteRepository.findAll().size();

        // Delete the docente
        restDocenteMockMvc.perform(delete("/api/docentes/{id}", docente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Docente> docenteList = docenteRepository.findAll();
        assertThat(docenteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Docente.class);
        Docente docente1 = new Docente();
        docente1.setId(1L);
        Docente docente2 = new Docente();
        docente2.setId(docente1.getId());
        assertThat(docente1).isEqualTo(docente2);
        docente2.setId(2L);
        assertThat(docente1).isNotEqualTo(docente2);
        docente1.setId(null);
        assertThat(docente1).isNotEqualTo(docente2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocenteDTO.class);
        DocenteDTO docenteDTO1 = new DocenteDTO();
        docenteDTO1.setId(1L);
        DocenteDTO docenteDTO2 = new DocenteDTO();
        assertThat(docenteDTO1).isNotEqualTo(docenteDTO2);
        docenteDTO2.setId(docenteDTO1.getId());
        assertThat(docenteDTO1).isEqualTo(docenteDTO2);
        docenteDTO2.setId(2L);
        assertThat(docenteDTO1).isNotEqualTo(docenteDTO2);
        docenteDTO1.setId(null);
        assertThat(docenteDTO1).isNotEqualTo(docenteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(docenteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(docenteMapper.fromId(null)).isNull();
    }
}
