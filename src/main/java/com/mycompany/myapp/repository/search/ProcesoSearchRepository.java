package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Proceso;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Proceso entity.
 */
public interface ProcesoSearchRepository extends ElasticsearchRepository<Proceso, Long> {
}
