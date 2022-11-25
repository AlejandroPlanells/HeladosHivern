package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ingrediente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ingrediente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long>, JpaSpecificationExecutor<Ingrediente> {}
