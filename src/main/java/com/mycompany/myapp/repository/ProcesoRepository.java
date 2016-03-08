package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Proceso;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Proceso entity.
 */
public interface ProcesoRepository extends JpaRepository<Proceso,Long> {

}
