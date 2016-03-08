package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Proceso;
import com.mycompany.myapp.repository.ProcesoRepository;
import com.mycompany.myapp.repository.search.ProcesoSearchRepository;

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
 * Test class for the ProcesoResource REST controller.
 *
 * @see ProcesoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProcesoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    @Inject
    private ProcesoRepository procesoRepository;

    @Inject
    private ProcesoSearchRepository procesoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProcesoMockMvc;

    private Proceso proceso;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProcesoResource procesoResource = new ProcesoResource();
        ReflectionTestUtils.setField(procesoResource, "procesoSearchRepository", procesoSearchRepository);
        ReflectionTestUtils.setField(procesoResource, "procesoRepository", procesoRepository);
        this.restProcesoMockMvc = MockMvcBuilders.standaloneSetup(procesoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        proceso = new Proceso();
        proceso.setNombre(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createProceso() throws Exception {
        int databaseSizeBeforeCreate = procesoRepository.findAll().size();

        // Create the Proceso

        restProcesoMockMvc.perform(post("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(proceso)))
                .andExpect(status().isCreated());

        // Validate the Proceso in the database
        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeCreate + 1);
        Proceso testProceso = procesos.get(procesos.size() - 1);
        assertThat(testProceso.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = procesoRepository.findAll().size();
        // set the field null
        proceso.setNombre(null);

        // Create the Proceso, which fails.

        restProcesoMockMvc.perform(post("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(proceso)))
                .andExpect(status().isBadRequest());

        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcesos() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

        // Get all the procesos
        restProcesoMockMvc.perform(get("/api/procesos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(proceso.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getProceso() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

        // Get the proceso
        restProcesoMockMvc.perform(get("/api/procesos/{id}", proceso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(proceso.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProceso() throws Exception {
        // Get the proceso
        restProcesoMockMvc.perform(get("/api/procesos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProceso() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

		int databaseSizeBeforeUpdate = procesoRepository.findAll().size();

        // Update the proceso
        proceso.setNombre(UPDATED_NOMBRE);

        restProcesoMockMvc.perform(put("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(proceso)))
                .andExpect(status().isOk());

        // Validate the Proceso in the database
        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeUpdate);
        Proceso testProceso = procesos.get(procesos.size() - 1);
        assertThat(testProceso.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void deleteProceso() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

		int databaseSizeBeforeDelete = procesoRepository.findAll().size();

        // Get the proceso
        restProcesoMockMvc.perform(delete("/api/procesos/{id}", proceso.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
