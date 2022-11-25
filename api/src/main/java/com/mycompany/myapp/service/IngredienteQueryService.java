package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Ingrediente;
import com.mycompany.myapp.repository.IngredienteRepository;
import com.mycompany.myapp.service.criteria.IngredienteCriteria;
import com.mycompany.myapp.service.dto.IngredienteDTO;
import com.mycompany.myapp.service.mapper.IngredienteMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ingrediente} entities in the database.
 * The main input is a {@link IngredienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IngredienteDTO} or a {@link Page} of {@link IngredienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IngredienteQueryService extends QueryService<Ingrediente> {

    private final Logger log = LoggerFactory.getLogger(IngredienteQueryService.class);

    private final IngredienteRepository ingredienteRepository;

    private final IngredienteMapper ingredienteMapper;

    public IngredienteQueryService(IngredienteRepository ingredienteRepository, IngredienteMapper ingredienteMapper) {
        this.ingredienteRepository = ingredienteRepository;
        this.ingredienteMapper = ingredienteMapper;
    }

    /**
     * Return a {@link List} of {@link IngredienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IngredienteDTO> findByCriteria(IngredienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ingrediente> specification = createSpecification(criteria);
        return ingredienteMapper.toDto(ingredienteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IngredienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IngredienteDTO> findByCriteria(IngredienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ingrediente> specification = createSpecification(criteria);
        return ingredienteRepository.findAll(specification, page).map(ingredienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IngredienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ingrediente> specification = createSpecification(criteria);
        return ingredienteRepository.count(specification);
    }

    /**
     * Function to convert {@link IngredienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ingrediente> createSpecification(IngredienteCriteria criteria) {
        Specification<Ingrediente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ingrediente_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Ingrediente_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Ingrediente_.descripcion));
            }
            if (criteria.getGr() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGr(), Ingrediente_.gr));
            }
            if (criteria.getCal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCal(), Ingrediente_.cal));
            }
            if (criteria.getHeladosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getHeladosId(), root -> root.join(Ingrediente_.helados, JoinType.LEFT).get(Helado_.id))
                    );
            }
        }
        return specification;
    }
}
