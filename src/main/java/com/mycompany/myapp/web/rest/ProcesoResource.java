package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Proceso;
import com.mycompany.myapp.repository.ProcesoRepository;
import com.mycompany.myapp.repository.search.ProcesoSearchRepository;
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
 * REST controller for managing Proceso.
 */
@RestController
@RequestMapping("/api")
public class ProcesoResource {

    private final Logger log = LoggerFactory.getLogger(ProcesoResource.class);
        
    @Inject
    private ProcesoRepository procesoRepository;
    
    @Inject
    private ProcesoSearchRepository procesoSearchRepository;
    
    /**
     * POST  /procesos -> Create a new proceso.
     */
    @RequestMapping(value = "/procesos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proceso> createProceso(@Valid @RequestBody Proceso proceso) throws URISyntaxException {
        log.debug("REST request to save Proceso : {}", proceso);
        if (proceso.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("proceso", "idexists", "A new proceso cannot already have an ID")).body(null);
        }
        Proceso result = procesoRepository.save(proceso);
        procesoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/procesos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("proceso", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procesos -> Updates an existing proceso.
     */
    @RequestMapping(value = "/procesos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proceso> updateProceso(@Valid @RequestBody Proceso proceso) throws URISyntaxException {
        log.debug("REST request to update Proceso : {}", proceso);
        if (proceso.getId() == null) {
            return createProceso(proceso);
        }
        Proceso result = procesoRepository.save(proceso);
        procesoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("proceso", proceso.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procesos -> get all the procesos.
     */
    @RequestMapping(value = "/procesos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Proceso> getAllProcesos() {
        log.debug("REST request to get all Procesos");
        return procesoRepository.findAll();
            }

    /**
     * GET  /procesos/:id -> get the "id" proceso.
     */
    @RequestMapping(value = "/procesos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proceso> getProceso(@PathVariable Long id) {
        log.debug("REST request to get Proceso : {}", id);
        Proceso proceso = procesoRepository.findOne(id);
        return Optional.ofNullable(proceso)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /procesos/:id -> delete the "id" proceso.
     */
    @RequestMapping(value = "/procesos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProceso(@PathVariable Long id) {
        log.debug("REST request to delete Proceso : {}", id);
        procesoRepository.delete(id);
        procesoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("proceso", id.toString())).build();
    }

    /**
     * SEARCH  /_search/procesos/:query -> search for the proceso corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/procesos/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Proceso> searchProcesos(@PathVariable String query) {
        log.debug("REST request to search Procesos for query {}", query);
        return StreamSupport
            .stream(procesoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
