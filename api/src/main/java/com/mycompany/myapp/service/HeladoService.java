package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.HeladoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Helado}.
 */
public interface HeladoService {
    /**
     * Save a helado.
     *
     * @param heladoDTO the entity to save.
     * @return the persisted entity.
     */
    HeladoDTO save(HeladoDTO heladoDTO);

    /**
     * Updates a helado.
     *
     * @param heladoDTO the entity to update.
     * @return the persisted entity.
     */
    HeladoDTO update(HeladoDTO heladoDTO);

    /**
     * Partially updates a helado.
     *
     * @param heladoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HeladoDTO> partialUpdate(HeladoDTO heladoDTO);

    /**
     * Get all the helados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HeladoDTO> findAll(Pageable pageable);

    /**
     * Get all the helados with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HeladoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" helado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HeladoDTO> findOne(Long id);

    /**
     * Delete the "id" helado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
