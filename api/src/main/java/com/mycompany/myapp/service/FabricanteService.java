package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.FabricanteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Fabricante}.
 */
public interface FabricanteService {
    /**
     * Save a fabricante.
     *
     * @param fabricanteDTO the entity to save.
     * @return the persisted entity.
     */
    FabricanteDTO save(FabricanteDTO fabricanteDTO);

    /**
     * Updates a fabricante.
     *
     * @param fabricanteDTO the entity to update.
     * @return the persisted entity.
     */
    FabricanteDTO update(FabricanteDTO fabricanteDTO);

    /**
     * Partially updates a fabricante.
     *
     * @param fabricanteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FabricanteDTO> partialUpdate(FabricanteDTO fabricanteDTO);

    /**
     * Get all the fabricantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FabricanteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fabricante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FabricanteDTO> findOne(Long id);

    /**
     * Delete the "id" fabricante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
