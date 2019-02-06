package it.filten.universita.web.rest;

import it.filten.universita.UniversitaApp;

import it.filten.universita.domain.Studente;
import it.filten.universita.domain.Facolta;
import it.filten.universita.domain.Corso;
import it.filten.universita.repository.StudenteRepository;
import it.filten.universita.service.StudenteService;
import it.filten.universita.service.dto.StudenteDTO;
import it.filten.universita.service.mapper.StudenteMapper;
import it.filten.universita.web.rest.errors.ExceptionTranslator;
import it.filten.universita.service.dto.StudenteCriteria;
import it.filten.universita.service.StudenteQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static it.filten.universita.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StudenteResource REST controller.
 *
 * @see StudenteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniversitaApp.class)
public class StudenteResourceIntTest {

    private static final LocalDate DEFAULT_DATA_NASCITA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCITA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COGNOME = "AAAAAAAAAA";
    private static final String UPDATED_COGNOME = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICOLA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICOLA = "BBBBBBBBBB";

    @Autowired
    private StudenteRepository studenteRepository;

    @Mock
    private StudenteRepository studenteRepositoryMock;

    @Autowired
    private StudenteMapper studenteMapper;

    @Mock
    private StudenteService studenteServiceMock;

    @Autowired
    private StudenteService studenteService;

    @Autowired
    private StudenteQueryService studenteQueryService;

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

    private MockMvc restStudenteMockMvc;

    private Studente studente;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudenteResource studenteResource = new StudenteResource(studenteService, studenteQueryService);
        this.restStudenteMockMvc = MockMvcBuilders.standaloneSetup(studenteResource)
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
    public static Studente createEntity(EntityManager em) {
        Studente studente = new Studente()
            .data_nascita(DEFAULT_DATA_NASCITA)
            .nome(DEFAULT_NOME)
            .cognome(DEFAULT_COGNOME)
            .matricola(DEFAULT_MATRICOLA);
        // Add required entity
        Facolta facolta = FacoltaResourceIntTest.createEntity(em);
        em.persist(facolta);
        em.flush();
        studente.setFacolta(facolta);
        return studente;
    }

    @Before
    public void initTest() {
        studente = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudente() throws Exception {
        int databaseSizeBeforeCreate = studenteRepository.findAll().size();

        // Create the Studente
        StudenteDTO studenteDTO = studenteMapper.toDto(studente);
        restStudenteMockMvc.perform(post("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isCreated());

        // Validate the Studente in the database
        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeCreate + 1);
        Studente testStudente = studenteList.get(studenteList.size() - 1);
        assertThat(testStudente.getData_nascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testStudente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testStudente.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testStudente.getMatricola()).isEqualTo(DEFAULT_MATRICOLA);
    }

    @Test
    @Transactional
    public void createStudenteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studenteRepository.findAll().size();

        // Create the Studente with an existing ID
        studente.setId(1L);
        StudenteDTO studenteDTO = studenteMapper.toDto(studente);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudenteMockMvc.perform(post("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Studente in the database
        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkData_nascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = studenteRepository.findAll().size();
        // set the field null
        studente.setData_nascita(null);

        // Create the Studente, which fails.
        StudenteDTO studenteDTO = studenteMapper.toDto(studente);

        restStudenteMockMvc.perform(post("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isBadRequest());

        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studenteRepository.findAll().size();
        // set the field null
        studente.setNome(null);

        // Create the Studente, which fails.
        StudenteDTO studenteDTO = studenteMapper.toDto(studente);

        restStudenteMockMvc.perform(post("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isBadRequest());

        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCognomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studenteRepository.findAll().size();
        // set the field null
        studente.setCognome(null);

        // Create the Studente, which fails.
        StudenteDTO studenteDTO = studenteMapper.toDto(studente);

        restStudenteMockMvc.perform(post("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isBadRequest());

        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMatricolaIsRequired() throws Exception {
        int databaseSizeBeforeTest = studenteRepository.findAll().size();
        // set the field null
        studente.setMatricola(null);

        // Create the Studente, which fails.
        StudenteDTO studenteDTO = studenteMapper.toDto(studente);

        restStudenteMockMvc.perform(post("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isBadRequest());

        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudenti() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList
        restStudenteMockMvc.perform(get("/api/studenti?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studente.getId().intValue())))
            .andExpect(jsonPath("$.[*].data_nascita").value(hasItem(DEFAULT_DATA_NASCITA.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME.toString())))
            .andExpect(jsonPath("$.[*].matricola").value(hasItem(DEFAULT_MATRICOLA.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllStudentiWithEagerRelationshipsIsEnabled() throws Exception {
        StudenteResource studenteResource = new StudenteResource(studenteServiceMock, studenteQueryService);
        when(studenteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restStudenteMockMvc = MockMvcBuilders.standaloneSetup(studenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStudenteMockMvc.perform(get("/api/studenti?eagerload=true"))
        .andExpect(status().isOk());

        verify(studenteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllStudentiWithEagerRelationshipsIsNotEnabled() throws Exception {
        StudenteResource studenteResource = new StudenteResource(studenteServiceMock, studenteQueryService);
            when(studenteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restStudenteMockMvc = MockMvcBuilders.standaloneSetup(studenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStudenteMockMvc.perform(get("/api/studenti?eagerload=true"))
        .andExpect(status().isOk());

            verify(studenteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getStudente() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get the studente
        restStudenteMockMvc.perform(get("/api/studenti/{id}", studente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studente.getId().intValue()))
            .andExpect(jsonPath("$.data_nascita").value(DEFAULT_DATA_NASCITA.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME.toString()))
            .andExpect(jsonPath("$.matricola").value(DEFAULT_MATRICOLA.toString()));
    }

    @Test
    @Transactional
    public void getAllStudentiByData_nascitaIsEqualToSomething() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where data_nascita equals to DEFAULT_DATA_NASCITA
        defaultStudenteShouldBeFound("data_nascita.equals=" + DEFAULT_DATA_NASCITA);

        // Get all the studenteList where data_nascita equals to UPDATED_DATA_NASCITA
        defaultStudenteShouldNotBeFound("data_nascita.equals=" + UPDATED_DATA_NASCITA);
    }

    @Test
    @Transactional
    public void getAllStudentiByData_nascitaIsInShouldWork() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where data_nascita in DEFAULT_DATA_NASCITA or UPDATED_DATA_NASCITA
        defaultStudenteShouldBeFound("data_nascita.in=" + DEFAULT_DATA_NASCITA + "," + UPDATED_DATA_NASCITA);

        // Get all the studenteList where data_nascita equals to UPDATED_DATA_NASCITA
        defaultStudenteShouldNotBeFound("data_nascita.in=" + UPDATED_DATA_NASCITA);
    }

    @Test
    @Transactional
    public void getAllStudentiByData_nascitaIsNullOrNotNull() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where data_nascita is not null
        defaultStudenteShouldBeFound("data_nascita.specified=true");

        // Get all the studenteList where data_nascita is null
        defaultStudenteShouldNotBeFound("data_nascita.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentiByData_nascitaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where data_nascita greater than or equals to DEFAULT_DATA_NASCITA
        defaultStudenteShouldBeFound("data_nascita.greaterOrEqualThan=" + DEFAULT_DATA_NASCITA);

        // Get all the studenteList where data_nascita greater than or equals to UPDATED_DATA_NASCITA
        defaultStudenteShouldNotBeFound("data_nascita.greaterOrEqualThan=" + UPDATED_DATA_NASCITA);
    }

    @Test
    @Transactional
    public void getAllStudentiByData_nascitaIsLessThanSomething() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where data_nascita less than or equals to DEFAULT_DATA_NASCITA
        defaultStudenteShouldNotBeFound("data_nascita.lessThan=" + DEFAULT_DATA_NASCITA);

        // Get all the studenteList where data_nascita less than or equals to UPDATED_DATA_NASCITA
        defaultStudenteShouldBeFound("data_nascita.lessThan=" + UPDATED_DATA_NASCITA);
    }


    @Test
    @Transactional
    public void getAllStudentiByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where nome equals to DEFAULT_NOME
        defaultStudenteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the studenteList where nome equals to UPDATED_NOME
        defaultStudenteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllStudentiByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultStudenteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the studenteList where nome equals to UPDATED_NOME
        defaultStudenteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllStudentiByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where nome is not null
        defaultStudenteShouldBeFound("nome.specified=true");

        // Get all the studenteList where nome is null
        defaultStudenteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentiByCognomeIsEqualToSomething() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where cognome equals to DEFAULT_COGNOME
        defaultStudenteShouldBeFound("cognome.equals=" + DEFAULT_COGNOME);

        // Get all the studenteList where cognome equals to UPDATED_COGNOME
        defaultStudenteShouldNotBeFound("cognome.equals=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    public void getAllStudentiByCognomeIsInShouldWork() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where cognome in DEFAULT_COGNOME or UPDATED_COGNOME
        defaultStudenteShouldBeFound("cognome.in=" + DEFAULT_COGNOME + "," + UPDATED_COGNOME);

        // Get all the studenteList where cognome equals to UPDATED_COGNOME
        defaultStudenteShouldNotBeFound("cognome.in=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    public void getAllStudentiByCognomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where cognome is not null
        defaultStudenteShouldBeFound("cognome.specified=true");

        // Get all the studenteList where cognome is null
        defaultStudenteShouldNotBeFound("cognome.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentiByMatricolaIsEqualToSomething() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where matricola equals to DEFAULT_MATRICOLA
        defaultStudenteShouldBeFound("matricola.equals=" + DEFAULT_MATRICOLA);

        // Get all the studenteList where matricola equals to UPDATED_MATRICOLA
        defaultStudenteShouldNotBeFound("matricola.equals=" + UPDATED_MATRICOLA);
    }

    @Test
    @Transactional
    public void getAllStudentiByMatricolaIsInShouldWork() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where matricola in DEFAULT_MATRICOLA or UPDATED_MATRICOLA
        defaultStudenteShouldBeFound("matricola.in=" + DEFAULT_MATRICOLA + "," + UPDATED_MATRICOLA);

        // Get all the studenteList where matricola equals to UPDATED_MATRICOLA
        defaultStudenteShouldNotBeFound("matricola.in=" + UPDATED_MATRICOLA);
    }

    @Test
    @Transactional
    public void getAllStudentiByMatricolaIsNullOrNotNull() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        // Get all the studenteList where matricola is not null
        defaultStudenteShouldBeFound("matricola.specified=true");

        // Get all the studenteList where matricola is null
        defaultStudenteShouldNotBeFound("matricola.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentiByFacoltaIsEqualToSomething() throws Exception {
        // Initialize the database
        Facolta facolta = FacoltaResourceIntTest.createEntity(em);
        em.persist(facolta);
        em.flush();
        studente.setFacolta(facolta);
        studenteRepository.saveAndFlush(studente);
        Long facoltaId = facolta.getId();

        // Get all the studenteList where facolta equals to facoltaId
        defaultStudenteShouldBeFound("facoltaId.equals=" + facoltaId);

        // Get all the studenteList where facolta equals to facoltaId + 1
        defaultStudenteShouldNotBeFound("facoltaId.equals=" + (facoltaId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentiByCorsiIsEqualToSomething() throws Exception {
        // Initialize the database
        Corso corsi = CorsoResourceIntTest.createEntity(em);
        em.persist(corsi);
        em.flush();
        studente.addCorsi(corsi);
        studenteRepository.saveAndFlush(studente);
        Long corsiId = corsi.getId();

        // Get all the studenteList where corsi equals to corsiId
        defaultStudenteShouldBeFound("corsiId.equals=" + corsiId);

        // Get all the studenteList where corsi equals to corsiId + 1
        defaultStudenteShouldNotBeFound("corsiId.equals=" + (corsiId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStudenteShouldBeFound(String filter) throws Exception {
        restStudenteMockMvc.perform(get("/api/studenti?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studente.getId().intValue())))
            .andExpect(jsonPath("$.[*].data_nascita").value(hasItem(DEFAULT_DATA_NASCITA.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)))
            .andExpect(jsonPath("$.[*].matricola").value(hasItem(DEFAULT_MATRICOLA)));

        // Check, that the count call also returns 1
        restStudenteMockMvc.perform(get("/api/studenti/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStudenteShouldNotBeFound(String filter) throws Exception {
        restStudenteMockMvc.perform(get("/api/studenti?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudenteMockMvc.perform(get("/api/studenti/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStudente() throws Exception {
        // Get the studente
        restStudenteMockMvc.perform(get("/api/studenti/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudente() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        int databaseSizeBeforeUpdate = studenteRepository.findAll().size();

        // Update the studente
        Studente updatedStudente = studenteRepository.findById(studente.getId()).get();
        // Disconnect from session so that the updates on updatedStudente are not directly saved in db
        em.detach(updatedStudente);
        updatedStudente
            .data_nascita(UPDATED_DATA_NASCITA)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .matricola(UPDATED_MATRICOLA);
        StudenteDTO studenteDTO = studenteMapper.toDto(updatedStudente);

        restStudenteMockMvc.perform(put("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isOk());

        // Validate the Studente in the database
        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeUpdate);
        Studente testStudente = studenteList.get(studenteList.size() - 1);
        assertThat(testStudente.getData_nascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testStudente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testStudente.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testStudente.getMatricola()).isEqualTo(UPDATED_MATRICOLA);
    }

    @Test
    @Transactional
    public void updateNonExistingStudente() throws Exception {
        int databaseSizeBeforeUpdate = studenteRepository.findAll().size();

        // Create the Studente
        StudenteDTO studenteDTO = studenteMapper.toDto(studente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudenteMockMvc.perform(put("/api/studenti")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Studente in the database
        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudente() throws Exception {
        // Initialize the database
        studenteRepository.saveAndFlush(studente);

        int databaseSizeBeforeDelete = studenteRepository.findAll().size();

        // Delete the studente
        restStudenteMockMvc.perform(delete("/api/studenti/{id}", studente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Studente> studenteList = studenteRepository.findAll();
        assertThat(studenteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Studente.class);
        Studente studente1 = new Studente();
        studente1.setId(1L);
        Studente studente2 = new Studente();
        studente2.setId(studente1.getId());
        assertThat(studente1).isEqualTo(studente2);
        studente2.setId(2L);
        assertThat(studente1).isNotEqualTo(studente2);
        studente1.setId(null);
        assertThat(studente1).isNotEqualTo(studente2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudenteDTO.class);
        StudenteDTO studenteDTO1 = new StudenteDTO();
        studenteDTO1.setId(1L);
        StudenteDTO studenteDTO2 = new StudenteDTO();
        assertThat(studenteDTO1).isNotEqualTo(studenteDTO2);
        studenteDTO2.setId(studenteDTO1.getId());
        assertThat(studenteDTO1).isEqualTo(studenteDTO2);
        studenteDTO2.setId(2L);
        assertThat(studenteDTO1).isNotEqualTo(studenteDTO2);
        studenteDTO1.setId(null);
        assertThat(studenteDTO1).isNotEqualTo(studenteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(studenteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(studenteMapper.fromId(null)).isNull();
    }
}
