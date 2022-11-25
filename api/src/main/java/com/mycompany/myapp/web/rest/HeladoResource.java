package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.HeladoRepository;
import com.mycompany.myapp.service.HeladoQueryService;
import com.mycompany.myapp.service.HeladoService;
import com.mycompany.myapp.service.criteria.HeladoCriteria;
import com.mycompany.myapp.service.dto.HeladoDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Helado}.
 */
@RestController
@RequestMapping("/api")
public class HeladoResource {

    private final Logger log = LoggerFactory.getLogger(HeladoResource.class);

    private static final String ENTITY_NAME = "helado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HeladoService heladoService;

    private final HeladoRepository heladoRepository;

    private final HeladoQueryService heladoQueryService;

    public HeladoResource(HeladoService heladoService, HeladoRepository heladoRepository, HeladoQueryService heladoQueryService) {
        this.heladoService = heladoService;
        this.heladoRepository = heladoRepository;
        this.heladoQueryService = heladoQueryService;
    }

    /**
     * {@code POST  /helados} : Create a new helado.
     *
     * @param heladoDTO the heladoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new heladoDTO, or with status {@code 400 (Bad Request)} if the helado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/helados")
    public ResponseEntity<HeladoDTO> createHelado(@Valid @RequestBody HeladoDTO heladoDTO) throws URISyntaxException {
        log.debug("REST request to save Helado : {}", heladoDTO);
        if (heladoDTO.getId() != null) {
            throw new BadRequestAlertException("A new helado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HeladoDTO result = heladoService.save(heladoDTO);
        return ResponseEntity
            .created(new URI("/api/helados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /helados/:id} : Updates an existing helado.
     *
     * @param id the id of the heladoDTO to save.
     * @param heladoDTO the heladoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated heladoDTO,
     * or with status {@code 400 (Bad Request)} if the heladoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the heladoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/helados/{id}")
    public ResponseEntity<HeladoDTO> updateHelado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HeladoDTO heladoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Helado : {}, {}", id, heladoDTO);
        if (heladoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, heladoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!heladoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HeladoDTO result = heladoService.update(heladoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, heladoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /helados/:id} : Partial updates given fields of an existing helado, field will ignore if it is null
     *
     * @param id the id of the heladoDTO to save.
     * @param heladoDTO the heladoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated heladoDTO,
     * or with status {@code 400 (Bad Request)} if the heladoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the heladoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the heladoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/helados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HeladoDTO> partialUpdateHelado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HeladoDTO heladoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Helado partially : {}, {}", id, heladoDTO);
        if (heladoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, heladoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!heladoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HeladoDTO> result = heladoService.partialUpdate(heladoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, heladoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /helados} : get all the helados.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helados in body.
     */
    @GetMapping("/helados")
    public ResponseEntity<List<HeladoDTO>> getAllHelados(
        HeladoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Helados by criteria: {}", criteria);
        Page<HeladoDTO> page = heladoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /helados/count} : count all the helados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/helados/count")
    public ResponseEntity<Long> countHelados(HeladoCriteria criteria) {
        log.debug("REST request to count Helados by criteria: {}", criteria);
        return ResponseEntity.ok().body(heladoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /helados/:id} : get the "id" helado.
     *
     * @param id the id of the heladoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the heladoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/helados/{id}")
    public ResponseEntity<HeladoDTO> getHelado(@PathVariable Long id) {
        log.debug("REST request to get Helado : {}", id);
        Optional<HeladoDTO> heladoDTO = heladoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(heladoDTO);
    }

    /**
     * {@code DELETE  /helados/:id} : delete the "id" helado.
     *
     * @param id the id of the heladoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/helados/{id}")
    public ResponseEntity<Void> deleteHelado(@PathVariable Long id) {
        log.debug("REST request to delete Helado : {}", id);
        heladoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
