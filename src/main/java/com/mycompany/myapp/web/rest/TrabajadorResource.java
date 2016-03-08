package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.TrabajadorRepository;
import com.mycompany.myapp.repository.search.TrabajadorSearchRepository;
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
 * REST controller for managing Trabajador.
 */
@RestController
@RequestMapping("/api")
public class TrabajadorResource {

    private final Logger log = LoggerFactory.getLogger(TrabajadorResource.class);
        
    @Inject
    private TrabajadorRepository trabajadorRepository;
    
    @Inject
    private TrabajadorSearchRepository trabajadorSearchRepository;
    
    /**
     * POST  /trabajadors -> Create a new trabajador.
     */
    @RequestMapping(value = "/trabajadors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajador> createTrabajador(@Valid @RequestBody Trabajador trabajador) throws URISyntaxException {
        log.debug("REST request to save Trabajador : {}", trabajador);
        if (trabajador.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trabajador", "idexists", "A new trabajador cannot already have an ID")).body(null);
        }
        Trabajador result = trabajadorRepository.save(trabajador);
        trabajadorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/trabajadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trabajador", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trabajadors -> Updates an existing trabajador.
     */
    @RequestMapping(value = "/trabajadors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajador> updateTrabajador(@Valid @RequestBody Trabajador trabajador) throws URISyntaxException {
        log.debug("REST request to update Trabajador : {}", trabajador);
        if (trabajador.getId() == null) {
            return createTrabajador(trabajador);
        }
        Trabajador result = trabajadorRepository.save(trabajador);
        trabajadorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trabajador", trabajador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trabajadors -> get all the trabajadors.
     */
    @RequestMapping(value = "/trabajadors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trabajador> getAllTrabajadors() {
        log.debug("REST request to get all Trabajadors");
        return trabajadorRepository.findAllWithEagerRelationships();
            }

    /**
     * GET  /trabajadors/:id -> get the "id" trabajador.
     */
    @RequestMapping(value = "/trabajadors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajador> getTrabajador(@PathVariable Long id) {
        log.debug("REST request to get Trabajador : {}", id);
        Trabajador trabajador = trabajadorRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(trabajador)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trabajadors/:id -> delete the "id" trabajador.
     */
    @RequestMapping(value = "/trabajadors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTrabajador(@PathVariable Long id) {
        log.debug("REST request to delete Trabajador : {}", id);
        trabajadorRepository.delete(id);
        trabajadorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trabajador", id.toString())).build();
    }

    /**
     * SEARCH  /_search/trabajadors/:query -> search for the trabajador corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/trabajadors/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trabajador> searchTrabajadors(@PathVariable String query) {
        log.debug("REST request to search Trabajadors for query {}", query);
        return StreamSupport
            .stream(trabajadorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
