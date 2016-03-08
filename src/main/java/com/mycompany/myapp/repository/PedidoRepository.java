package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pedido;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    @Query("select pedido from Pedido pedido where pedido.user.login = ?#{principal.username}")
    List<Pedido> findByUserIsCurrentUser();

    @Query("select distinct pedido from Pedido pedido left join fetch pedido.procesos left join fetch pedido.trabajadors")
    List<Pedido> findAllWithEagerRelationships();

    @Query("select pedido from Pedido pedido left join fetch pedido.procesos left join fetch pedido.trabajadors where pedido.id =:id")
    Pedido findOneWithEagerRelationships(@Param("id") Long id);

}
