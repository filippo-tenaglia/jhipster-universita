package it.filten.universita.web.rest;

import it.filten.universita.UniversitaApp;

import it.filten.universita.domain.Facolta;
import it.filten.universita.repository.FacoltaRepository;
import it.filten.universita.service.FacoltaService;
import it.filten.universita.service.dto.FacoltaDTO;
import it.filten.universita.service.mapper.FacoltaMapper;
import it.filten.universita.web.rest.errors.ExceptionTranslator;
import it.filten.universita.service.dto.FacoltaCriteria;
import it.filten.universita.service.FacoltaQueryService;

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
 * Test class for the FacoltaResource REST controller.
 *
 * @see FacoltaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniversitaApp.class)
public class FacoltaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private FacoltaRepository facoltaRepository;

    @Autowired
    private FacoltaMapper facoltaMapper;

    @Autowired
    private FacoltaService facoltaService;

    @Autowired
    private FacoltaQueryService facoltaQueryService;

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

    private MockMvc restFacoltaMockMvc;

    private Facolta facolta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FacoltaResource facoltaResource = new FacoltaResource(facoltaService, facoltaQueryService);
        this.restFacoltaMockMvc = MockMvcBuilders.standaloneSetup(facoltaResource)
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
    public static Facolta createEntity(EntityManager em) {
        Facolta facolta = new Facolta()
            .nome(DEFAULT_NOME);
        return facolta;
    }

    @Before
    public void initTest() {
        facolta = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacolta() throws Exception {
        int databaseSizeBeforeCreate = facoltaRepository.findAll().size();

        // Create the Facolta
        FacoltaDTO facoltaDTO = facoltaMapper.toDto(facolta);
        restFacoltaMockMvc.perform(post("/api/facoltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facoltaDTO)))
            .andExpect(status().isCreated());

        // Validate the Facolta in the database
        List<Facolta> facoltaList = facoltaRepository.findAll();
        assertThat(facoltaList).hasSize(databaseSizeBeforeCreate + 1);
        Facolta testFacolta = facoltaList.get(facoltaList.size() - 1);
        assertThat(testFacolta.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createFacoltaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facoltaRepository.findAll().size();

        // Create the Facolta with an existing ID
        facolta.setId(1L);
        FacoltaDTO facoltaDTO = facoltaMapper.toDto(facolta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacoltaMockMvc.perform(post("/api/facoltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facoltaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facolta in the database
        List<Facolta> facoltaList = facoltaRepository.findAll();
        assertThat(facoltaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFacoltas() throws Exception {
        // Initialize the database
        facoltaRepository.saveAndFlush(facolta);

        // Get all the facoltaList
        restFacoltaMockMvc.perform(get("/api/facoltas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facolta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }
    
    @Test
    @Transactional
    public void getFacolta() throws Exception {
        // Initialize the database
        facoltaRepository.saveAndFlush(facolta);

        // Get the facolta
        restFacoltaMockMvc.perform(get("/api/facoltas/{id}", facolta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facolta.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllFacoltasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        facoltaRepository.saveAndFlush(facolta);

        // Get all the facoltaList where nome equals to DEFAULT_NOME
        defaultFacoltaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the facoltaList where nome equals to UPDATED_NOME
        defaultFacoltaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllFacoltasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        facoltaRepository.saveAndFlush(facolta);

        // Get all the facoltaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultFacoltaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the facoltaList where nome equals to UPDATED_NOME
        defaultFacoltaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllFacoltasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        facoltaRepository.saveAndFlush(facolta);

        // Get all the facoltaList where nome is not null
        defaultFacoltaShouldBeFound("nome.specified=true");

        // Get all the facoltaList where nome is null
        defaultFacoltaShouldNotBeFound("nome.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFacoltaShouldBeFound(String filter) throws Exception {
        restFacoltaMockMvc.perform(get("/api/facoltas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facolta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));

        // Check, that the count call also returns 1
        restFacoltaMockMvc.perform(get("/api/facoltas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFacoltaShouldNotBeFound(String filter) throws Exception {
        restFacoltaMockMvc.perform(get("/api/facoltas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacoltaMockMvc.perform(get("/api/facoltas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFacolta() throws Exception {
        // Get the facolta
        restFacoltaMockMvc.perform(get("/api/facoltas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacolta() throws Exception {
        // Initialize the database
        facoltaRepository.saveAndFlush(facolta);

        int databaseSizeBeforeUpdate = facoltaRepository.findAll().size();

        // Update the facolta
        Facolta updatedFacolta = facoltaRepository.findById(facolta.getId()).get();
        // Disconnect from session so that the updates on updatedFacolta are not directly saved in db
        em.detach(updatedFacolta);
        updatedFacolta
            .nome(UPDATED_NOME);
        FacoltaDTO facoltaDTO = facoltaMapper.toDto(updatedFacolta);

        restFacoltaMockMvc.perform(put("/api/facoltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facoltaDTO)))
            .andExpect(status().isOk());

        // Validate the Facolta in the database
        List<Facolta> facoltaList = facoltaRepository.findAll();
        assertThat(facoltaList).hasSize(databaseSizeBeforeUpdate);
        Facolta testFacolta = facoltaList.get(facoltaList.size() - 1);
        assertThat(testFacolta.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingFacolta() throws Exception {
        int databaseSizeBeforeUpdate = facoltaRepository.findAll().size();

        // Create the Facolta
        FacoltaDTO facoltaDTO = facoltaMapper.toDto(facolta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacoltaMockMvc.perform(put("/api/facoltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facoltaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facolta in the database
        List<Facolta> facoltaList = facoltaRepository.findAll();
        assertThat(facoltaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFacolta() throws Exception {
        // Initialize the database
        facoltaRepository.saveAndFlush(facolta);

        int databaseSizeBeforeDelete = facoltaRepository.findAll().size();

        // Delete the facolta
        restFacoltaMockMvc.perform(delete("/api/facoltas/{id}", facolta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Facolta> facoltaList = facoltaRepository.findAll();
        assertThat(facoltaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facolta.class);
        Facolta facolta1 = new Facolta();
        facolta1.setId(1L);
        Facolta facolta2 = new Facolta();
        facolta2.setId(facolta1.getId());
        assertThat(facolta1).isEqualTo(facolta2);
        facolta2.setId(2L);
        assertThat(facolta1).isNotEqualTo(facolta2);
        facolta1.setId(null);
        assertThat(facolta1).isNotEqualTo(facolta2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacoltaDTO.class);
        FacoltaDTO facoltaDTO1 = new FacoltaDTO();
        facoltaDTO1.setId(1L);
        FacoltaDTO facoltaDTO2 = new FacoltaDTO();
        assertThat(facoltaDTO1).isNotEqualTo(facoltaDTO2);
        facoltaDTO2.setId(facoltaDTO1.getId());
        assertThat(facoltaDTO1).isEqualTo(facoltaDTO2);
        facoltaDTO2.setId(2L);
        assertThat(facoltaDTO1).isNotEqualTo(facoltaDTO2);
        facoltaDTO1.setId(null);
        assertThat(facoltaDTO1).isNotEqualTo(facoltaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(facoltaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(facoltaMapper.fromId(null)).isNull();
    }
}
