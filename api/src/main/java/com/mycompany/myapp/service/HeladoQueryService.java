package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Helado;
import com.mycompany.myapp.repository.HeladoRepository;
import com.mycompany.myapp.service.criteria.HeladoCriteria;
import com.mycompany.myapp.service.dto.HeladoDTO;
import com.mycompany.myapp.service.mapper.HeladoMapper;
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
 * Service for executing complex queries for {@link Helado} entities in the database.
 * The main input is a {@link HeladoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HeladoDTO} or a {@link Page} of {@link HeladoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HeladoQueryService extends QueryService<Helado> {

    private final Logger log = LoggerFactory.getLogger(HeladoQueryService.class);

    private final HeladoRepository heladoRepository;

    private final HeladoMapper heladoMapper;

    public HeladoQueryService(HeladoRepository heladoRepository, HeladoMapper heladoMapper) {
        this.heladoRepository = heladoRepository;
        this.heladoMapper = heladoMapper;
    }

    /**
     * Return a {@link List} of {@link HeladoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HeladoDTO> findByCriteria(HeladoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Helado> specification = createSpecification(criteria);
        return heladoMapper.toDto(heladoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HeladoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HeladoDTO> findByCriteria(HeladoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Helado> specification = createSpecification(criteria);
        return heladoRepository.findAll(specification, page).map(heladoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HeladoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Helado> specification = createSpecification(criteria);
        return heladoRepository.count(specification);
    }

    /**
     * Function to convert {@link HeladoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Helado> createSpecification(HeladoCriteria criteria) {
        Specification<Helado> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Helado_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Helado_.nombre));
            }
            if (criteria.getEnOferta() != null) {
                specification = specification.and(buildSpecification(criteria.getEnOferta(), Helado_.enOferta));
            }
            if (criteria.getPrecioOferta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecioOferta(), Helado_.precioOferta));
            }
            if (criteria.getPrecio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecio(), Helado_.precio));
            }
            if (criteria.getFechaCreacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCreacion(), Helado_.fechaCreacion));
            }
            if (criteria.getFabricanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFabricanteId(),
                            root -> root.join(Helado_.fabricante, JoinType.LEFT).get(Fabricante_.id)
                        )
                    );
            }
            if (criteria.getIngredientesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIngredientesId(),
                            root -> root.join(Helado_.ingredientes, JoinType.LEFT).get(Ingrediente_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
