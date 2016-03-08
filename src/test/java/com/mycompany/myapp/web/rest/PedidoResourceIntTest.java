package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Pedido;
import com.mycompany.myapp.repository.PedidoRepository;
import com.mycompany.myapp.repository.search.PedidoSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PedidoResource REST controller.
 *
 * @see PedidoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PedidoResourceIntTest {

    
    private static final String DEFAULT_DESCRIPCION = "";
    private static final String UPDATED_DESCRIPCION = "";

    private static final Integer DEFAULT_COSTE_SIN_IVA = 1;
    private static final Integer UPDATED_COSTE_SIN_IVA = 2;

    private static final Integer DEFAULT_COSTE_TOTAL = 1;
    private static final Integer UPDATED_COSTE_TOTAL = 2;

    @Inject
    private PedidoRepository pedidoRepository;

    @Inject
    private PedidoSearchRepository pedidoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PedidoResource pedidoResource = new PedidoResource();
        ReflectionTestUtils.setField(pedidoResource, "pedidoSearchRepository", pedidoSearchRepository);
        ReflectionTestUtils.setField(pedidoResource, "pedidoRepository", pedidoRepository);
        this.restPedidoMockMvc = MockMvcBuilders.standaloneSetup(pedidoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pedido = new Pedido();
        pedido.setDescripcion(DEFAULT_DESCRIPCION);
        pedido.setCosteSinIva(DEFAULT_COSTE_SIN_IVA);
        pedido.setCosteTotal(DEFAULT_COSTE_TOTAL);
    }

    @Test
    @Transactional
    public void createPedido() throws Exception {
        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();

        // Create the Pedido

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isCreated());

        // Validate the Pedido in the database
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeCreate + 1);
        Pedido testPedido = pedidos.get(pedidos.size() - 1);
        assertThat(testPedido.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPedido.getCosteSinIva()).isEqualTo(DEFAULT_COSTE_SIN_IVA);
        assertThat(testPedido.getCosteTotal()).isEqualTo(DEFAULT_COSTE_TOTAL);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidoRepository.findAll().size();
        // set the field null
        pedido.setDescripcion(null);

        // Create the Pedido, which fails.

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isBadRequest());

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCosteSinIvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidoRepository.findAll().size();
        // set the field null
        pedido.setCosteSinIva(null);

        // Create the Pedido, which fails.

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isBadRequest());

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCosteTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidoRepository.findAll().size();
        // set the field null
        pedido.setCosteTotal(null);

        // Create the Pedido, which fails.

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isBadRequest());

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPedidos() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidos
        restPedidoMockMvc.perform(get("/api/pedidos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].costeSinIva").value(hasItem(DEFAULT_COSTE_SIN_IVA)))
                .andExpect(jsonPath("$.[*].costeTotal").value(hasItem(DEFAULT_COSTE_TOTAL)));
    }

    @Test
    @Transactional
    public void getPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.costeSinIva").value(DEFAULT_COSTE_SIN_IVA))
            .andExpect(jsonPath("$.costeTotal").value(DEFAULT_COSTE_TOTAL));
    }

    @Test
    @Transactional
    public void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

		int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido
        pedido.setDescripcion(UPDATED_DESCRIPCION);
        pedido.setCosteSinIva(UPDATED_COSTE_SIN_IVA);
        pedido.setCosteTotal(UPDATED_COSTE_TOTAL);

        restPedidoMockMvc.perform(put("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidos.get(pedidos.size() - 1);
        assertThat(testPedido.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPedido.getCosteSinIva()).isEqualTo(UPDATED_COSTE_SIN_IVA);
        assertThat(testPedido.getCosteTotal()).isEqualTo(UPDATED_COSTE_TOTAL);
    }

    @Test
    @Transactional
    public void deletePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

		int databaseSizeBeforeDelete = pedidoRepository.findAll().size();

        // Get the pedido
        restPedidoMockMvc.perform(delete("/api/pedidos/{id}", pedido.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
