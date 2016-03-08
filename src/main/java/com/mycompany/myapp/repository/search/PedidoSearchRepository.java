package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Pedido;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pedido entity.
 */
public interface PedidoSearchRepository extends ElasticsearchRepository<Pedido, Long> {
}
