package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajador;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Trabajador entity.
 */
public interface TrabajadorRepository extends JpaRepository<Trabajador,Long> {

    @Query("select distinct trabajador from Trabajador trabajador left join fetch trabajador.procesos")
    List<Trabajador> findAllWithEagerRelationships();

    @Query("select trabajador from Trabajador trabajador left join fetch trabajador.procesos where trabajador.id =:id")
    Trabajador findOneWithEagerRelationships(@Param("id") Long id);

}
