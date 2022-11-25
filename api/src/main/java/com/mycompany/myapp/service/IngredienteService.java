package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.IngredienteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Ingrediente}.
 */
public interface IngredienteService {
    /**
     * Save a ingrediente.
     *
     * @param ingredienteDTO the entity to save.
     * @return the persisted entity.
     */
    IngredienteDTO save(IngredienteDTO ingredienteDTO);

    /**
     * Updates a ingrediente.
     *
     * @param ingredienteDTO the entity to update.
     * @return the persisted entity.
     */
    IngredienteDTO update(IngredienteDTO ingredienteDTO);

    /**
     * Partially updates a ingrediente.
     *
     * @param ingredienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IngredienteDTO> partialUpdate(IngredienteDTO ingredienteDTO);

    /**
     * Get all the ingredientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IngredienteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ingrediente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IngredienteDTO> findOne(Long id);

    /**
     * Delete the "id" ingrediente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
