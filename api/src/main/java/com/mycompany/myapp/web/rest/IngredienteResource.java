package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.IngredienteRepository;
import com.mycompany.myapp.service.IngredienteQueryService;
import com.mycompany.myapp.service.IngredienteService;
import com.mycompany.myapp.service.criteria.IngredienteCriteria;
import com.mycompany.myapp.service.dto.IngredienteDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Ingrediente}.
 */
@RestController
@RequestMapping("/api")
public class IngredienteResource {

    private final Logger log = LoggerFactory.getLogger(IngredienteResource.class);

    private static final String ENTITY_NAME = "ingrediente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngredienteService ingredienteService;

    private final IngredienteRepository ingredienteRepository;

    private final IngredienteQueryService ingredienteQueryService;

    public IngredienteResource(
        IngredienteService ingredienteService,
        IngredienteRepository ingredienteRepository,
        IngredienteQueryService ingredienteQueryService
    ) {
        this.ingredienteService = ingredienteService;
        this.ingredienteRepository = ingredienteRepository;
        this.ingredienteQueryService = ingredienteQueryService;
    }

    /**
     * {@code POST  /ingredientes} : Create a new ingrediente.
     *
     * @param ingredienteDTO the ingredienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredienteDTO, or with status {@code 400 (Bad Request)} if the ingrediente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingredientes")
    public ResponseEntity<IngredienteDTO> createIngrediente(@Valid @RequestBody IngredienteDTO ingredienteDTO) throws URISyntaxException {
        log.debug("REST request to save Ingrediente : {}", ingredienteDTO);
        if (ingredienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingrediente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IngredienteDTO result = ingredienteService.save(ingredienteDTO);
        return ResponseEntity
            .created(new URI("/api/ingredientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ingredientes/:id} : Updates an existing ingrediente.
     *
     * @param id the id of the ingredienteDTO to save.
     * @param ingredienteDTO the ingredienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredienteDTO,
     * or with status {@code 400 (Bad Request)} if the ingredienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredientes/{id}")
    public ResponseEntity<IngredienteDTO> updateIngrediente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngredienteDTO ingredienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ingrediente : {}, {}", id, ingredienteDTO);
        if (ingredienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IngredienteDTO result = ingredienteService.update(ingredienteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredienteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ingredientes/:id} : Partial updates given fields of an existing ingrediente, field will ignore if it is null
     *
     * @param id the id of the ingredienteDTO to save.
     * @param ingredienteDTO the ingredienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredienteDTO,
     * or with status {@code 400 (Bad Request)} if the ingredienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ingredienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingredienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingredientes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngredienteDTO> partialUpdateIngrediente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngredienteDTO ingredienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingrediente partially : {}, {}", id, ingredienteDTO);
        if (ingredienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngredienteDTO> result = ingredienteService.partialUpdate(ingredienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ingredientes} : get all the ingredientes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredientes in body.
     */
    @GetMapping("/ingredientes")
    public ResponseEntity<List<IngredienteDTO>> getAllIngredientes(
        IngredienteCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Ingredientes by criteria: {}", criteria);
        Page<IngredienteDTO> page = ingredienteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ingredientes/count} : count all the ingredientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ingredientes/count")
    public ResponseEntity<Long> countIngredientes(IngredienteCriteria criteria) {
        log.debug("REST request to count Ingredientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(ingredienteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ingredientes/:id} : get the "id" ingrediente.
     *
     * @param id the id of the ingredienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredientes/{id}")
    public ResponseEntity<IngredienteDTO> getIngrediente(@PathVariable Long id) {
        log.debug("REST request to get Ingrediente : {}", id);
        Optional<IngredienteDTO> ingredienteDTO = ingredienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingredienteDTO);
    }

    /**
     * {@code DELETE  /ingredientes/:id} : delete the "id" ingrediente.
     *
     * @param id the id of the ingredienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredientes/{id}")
    public ResponseEntity<Void> deleteIngrediente(@PathVariable Long id) {
        log.debug("REST request to delete Ingrediente : {}", id);
        ingredienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
