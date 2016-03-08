package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Pedido;
import com.mycompany.myapp.repository.PedidoRepository;
import com.mycompany.myapp.repository.search.PedidoSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Pedido.
 */
@RestController
@RequestMapping("/api")
public class PedidoResource {

    private final Logger log = LoggerFactory.getLogger(PedidoResource.class);

    @Inject
    private PedidoRepository pedidoRepository;

    @Inject
    private PedidoSearchRepository pedidoSearchRepository;

    /**
     * POST  /pedidos -> Create a new pedido.
     */
    @RequestMapping(value = "/pedidos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) throws URISyntaxException {
        log.debug("REST request to save Pedido : {}", pedido);
        if (pedido.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pedido", "idexists", "A new pedido cannot already have an ID")).body(null);
        }
        Pedido result = pedidoRepository.save(pedido);
        pedidoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pedido", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pedidos -> Updates an existing pedido.
     */
    @RequestMapping(value = "/pedidos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pedido> updatePedido(@Valid @RequestBody Pedido pedido) throws URISyntaxException {
        log.debug("REST request to update Pedido : {}", pedido);
        if (pedido.getId() == null) {
            return createPedido(pedido);
        }
        Pedido result = pedidoRepository.save(pedido);
        pedidoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pedido", pedido.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pedidos -> get all the pedidos.
     */
    @RequestMapping(value = "/pedidos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pedido> getAllPedidos() {
        log.debug("REST request to get all Pedidos");
        List<Pedido> pedidos=pedidoRepository.findByUserIsCurrentUser();
        return pedidos;
    }

    /**
     * GET  /pedidos/:id -> get the "id" pedido.
     */
    @RequestMapping(value = "/pedidos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pedido> getPedido(@PathVariable Long id) {
        log.debug("REST request to get Pedido : {}", id);
        Pedido pedido = pedidoRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(pedido)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pedidos/:id -> delete the "id" pedido.
     */
    @RequestMapping(value = "/pedidos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        log.debug("REST request to delete Pedido : {}", id);
        pedidoRepository.delete(id);
        pedidoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pedido", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pedidos/:query -> search for the pedido corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/pedidos/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pedido> searchPedidos(@PathVariable String query) {
        log.debug("REST request to search Pedidos for query {}", query);
        return StreamSupport
            .stream(pedidoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
