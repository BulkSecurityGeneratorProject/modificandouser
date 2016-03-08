package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.TrabajadorRepository;
import com.mycompany.myapp.repository.search.TrabajadorSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TrabajadorResource REST controller.
 *
 * @see TrabajadorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TrabajadorResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_DNI = "AAAAA";
    private static final String UPDATED_DNI = "BBBBB";

    private static final Integer DEFAULT_NUMERO_TELF = 1;
    private static final Integer UPDATED_NUMERO_TELF = 2;

    @Inject
    private TrabajadorRepository trabajadorRepository;

    @Inject
    private TrabajadorSearchRepository trabajadorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrabajadorMockMvc;

    private Trabajador trabajador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrabajadorResource trabajadorResource = new TrabajadorResource();
        ReflectionTestUtils.setField(trabajadorResource, "trabajadorSearchRepository", trabajadorSearchRepository);
        ReflectionTestUtils.setField(trabajadorResource, "trabajadorRepository", trabajadorRepository);
        this.restTrabajadorMockMvc = MockMvcBuilders.standaloneSetup(trabajadorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        trabajador = new Trabajador();
        trabajador.setNombre(DEFAULT_NOMBRE);
        trabajador.setEmail(DEFAULT_EMAIL);
        trabajador.setDni(DEFAULT_DNI);
        trabajador.setNumeroTelf(DEFAULT_NUMERO_TELF);
    }

    @Test
    @Transactional
    public void createTrabajador() throws Exception {
        int databaseSizeBeforeCreate = trabajadorRepository.findAll().size();

        // Create the Trabajador

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajador)))
                .andExpect(status().isCreated());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadors = trabajadorRepository.findAll();
        assertThat(trabajadors).hasSize(databaseSizeBeforeCreate + 1);
        Trabajador testTrabajador = trabajadors.get(trabajadors.size() - 1);
        assertThat(testTrabajador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTrabajador.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTrabajador.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testTrabajador.getNumeroTelf()).isEqualTo(DEFAULT_NUMERO_TELF);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setNombre(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajador)))
                .andExpect(status().isBadRequest());

        List<Trabajador> trabajadors = trabajadorRepository.findAll();
        assertThat(trabajadors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setEmail(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajador)))
                .andExpect(status().isBadRequest());

        List<Trabajador> trabajadors = trabajadorRepository.findAll();
        assertThat(trabajadors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setDni(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajador)))
                .andExpect(status().isBadRequest());

        List<Trabajador> trabajadors = trabajadorRepository.findAll();
        assertThat(trabajadors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroTelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajadorRepository.findAll().size();
        // set the field null
        trabajador.setNumeroTelf(null);

        // Create the Trabajador, which fails.

        restTrabajadorMockMvc.perform(post("/api/trabajadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajador)))
                .andExpect(status().isBadRequest());

        List<Trabajador> trabajadors = trabajadorRepository.findAll();
        assertThat(trabajadors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrabajadors() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadors
        restTrabajadorMockMvc.perform(get("/api/trabajadors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.toString())))
                .andExpect(jsonPath("$.[*].numeroTelf").value(hasItem(DEFAULT_NUMERO_TELF)));
    }

    @Test
    @Transactional
    public void getTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

        // Get the trabajador
        restTrabajadorMockMvc.perform(get("/api/trabajadors/{id}", trabajador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(trabajador.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI.toString()))
            .andExpect(jsonPath("$.numeroTelf").value(DEFAULT_NUMERO_TELF));
    }

    @Test
    @Transactional
    public void getNonExistingTrabajador() throws Exception {
        // Get the trabajador
        restTrabajadorMockMvc.perform(get("/api/trabajadors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

		int databaseSizeBeforeUpdate = trabajadorRepository.findAll().size();

        // Update the trabajador
        trabajador.setNombre(UPDATED_NOMBRE);
        trabajador.setEmail(UPDATED_EMAIL);
        trabajador.setDni(UPDATED_DNI);
        trabajador.setNumeroTelf(UPDATED_NUMERO_TELF);

        restTrabajadorMockMvc.perform(put("/api/trabajadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajador)))
                .andExpect(status().isOk());

        // Validate the Trabajador in the database
        List<Trabajador> trabajadors = trabajadorRepository.findAll();
        assertThat(trabajadors).hasSize(databaseSizeBeforeUpdate);
        Trabajador testTrabajador = trabajadors.get(trabajadors.size() - 1);
        assertThat(testTrabajador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTrabajador.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTrabajador.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testTrabajador.getNumeroTelf()).isEqualTo(UPDATED_NUMERO_TELF);
    }

    @Test
    @Transactional
    public void deleteTrabajador() throws Exception {
        // Initialize the database
        trabajadorRepository.saveAndFlush(trabajador);

		int databaseSizeBeforeDelete = trabajadorRepository.findAll().size();

        // Get the trabajador
        restTrabajadorMockMvc.perform(delete("/api/trabajadors/{id}", trabajador.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Trabajador> trabajadors = trabajadorRepository.findAll();
        assertThat(trabajadors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
