package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Trabajador;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Trabajador entity.
 */
public interface TrabajadorSearchRepository extends ElasticsearchRepository<Trabajador, Long> {
}
