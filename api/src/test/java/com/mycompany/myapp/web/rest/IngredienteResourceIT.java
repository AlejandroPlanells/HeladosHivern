package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Helado;
import com.mycompany.myapp.domain.Ingrediente;
import com.mycompany.myapp.repository.IngredienteRepository;
import com.mycompany.myapp.service.criteria.IngredienteCriteria;
import com.mycompany.myapp.service.dto.IngredienteDTO;
import com.mycompany.myapp.service.mapper.IngredienteMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IngredienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngredienteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_GR = 1F;
    private static final Float UPDATED_GR = 2F;
    private static final Float SMALLER_GR = 1F - 1F;

    private static final String DEFAULT_CAL = "AAAAAAAAAA";
    private static final String UPDATED_CAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ingredientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private IngredienteMapper ingredienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredienteMockMvc;

    private Ingrediente ingrediente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingrediente createEntity(EntityManager em) {
        Ingrediente ingrediente = new Ingrediente().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION).gr(DEFAULT_GR).cal(DEFAULT_CAL);
        return ingrediente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingrediente createUpdatedEntity(EntityManager em) {
        Ingrediente ingrediente = new Ingrediente().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).gr(UPDATED_GR).cal(UPDATED_CAL);
        return ingrediente;
    }

    @BeforeEach
    public void initTest() {
        ingrediente = createEntity(em);
    }

    @Test
    @Transactional
    void createIngrediente() throws Exception {
        int databaseSizeBeforeCreate = ingredienteRepository.findAll().size();
        // Create the Ingrediente
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);
        restIngredienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeCreate + 1);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testIngrediente.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testIngrediente.getGr()).isEqualTo(DEFAULT_GR);
        assertThat(testIngrediente.getCal()).isEqualTo(DEFAULT_CAL);
    }

    @Test
    @Transactional
    void createIngredienteWithExistingId() throws Exception {
        // Create the Ingrediente with an existing ID
        ingrediente.setId(1L);
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        int databaseSizeBeforeCreate = ingredienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredienteRepository.findAll().size();
        // set the field null
        ingrediente.setNombre(null);

        // Create the Ingrediente, which fails.
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        restIngredienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredienteRepository.findAll().size();
        // set the field null
        ingrediente.setDescripcion(null);

        // Create the Ingrediente, which fails.
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        restIngredienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredienteRepository.findAll().size();
        // set the field null
        ingrediente.setGr(null);

        // Create the Ingrediente, which fails.
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        restIngredienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredienteRepository.findAll().size();
        // set the field null
        ingrediente.setCal(null);

        // Create the Ingrediente, which fails.
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        restIngredienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngredientes() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingrediente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].gr").value(hasItem(DEFAULT_GR.doubleValue())))
            .andExpect(jsonPath("$.[*].cal").value(hasItem(DEFAULT_CAL)));
    }

    @Test
    @Transactional
    void getIngrediente() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get the ingrediente
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL_ID, ingrediente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingrediente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.gr").value(DEFAULT_GR.doubleValue()))
            .andExpect(jsonPath("$.cal").value(DEFAULT_CAL));
    }

    @Test
    @Transactional
    void getIngredientesByIdFiltering() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        Long id = ingrediente.getId();

        defaultIngredienteShouldBeFound("id.equals=" + id);
        defaultIngredienteShouldNotBeFound("id.notEquals=" + id);

        defaultIngredienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIngredienteShouldNotBeFound("id.greaterThan=" + id);

        defaultIngredienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIngredienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIngredientesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where nombre equals to DEFAULT_NOMBRE
        defaultIngredienteShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the ingredienteList where nombre equals to UPDATED_NOMBRE
        defaultIngredienteShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIngredientesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultIngredienteShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the ingredienteList where nombre equals to UPDATED_NOMBRE
        defaultIngredienteShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIngredientesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where nombre is not null
        defaultIngredienteShouldBeFound("nombre.specified=true");

        // Get all the ingredienteList where nombre is null
        defaultIngredienteShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientesByNombreContainsSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where nombre contains DEFAULT_NOMBRE
        defaultIngredienteShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the ingredienteList where nombre contains UPDATED_NOMBRE
        defaultIngredienteShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIngredientesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where nombre does not contain DEFAULT_NOMBRE
        defaultIngredienteShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the ingredienteList where nombre does not contain UPDATED_NOMBRE
        defaultIngredienteShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIngredientesByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where descripcion equals to DEFAULT_DESCRIPCION
        defaultIngredienteShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the ingredienteList where descripcion equals to UPDATED_DESCRIPCION
        defaultIngredienteShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllIngredientesByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultIngredienteShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the ingredienteList where descripcion equals to UPDATED_DESCRIPCION
        defaultIngredienteShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllIngredientesByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where descripcion is not null
        defaultIngredienteShouldBeFound("descripcion.specified=true");

        // Get all the ingredienteList where descripcion is null
        defaultIngredienteShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientesByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where descripcion contains DEFAULT_DESCRIPCION
        defaultIngredienteShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the ingredienteList where descripcion contains UPDATED_DESCRIPCION
        defaultIngredienteShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllIngredientesByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultIngredienteShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the ingredienteList where descripcion does not contain UPDATED_DESCRIPCION
        defaultIngredienteShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllIngredientesByGrIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where gr equals to DEFAULT_GR
        defaultIngredienteShouldBeFound("gr.equals=" + DEFAULT_GR);

        // Get all the ingredienteList where gr equals to UPDATED_GR
        defaultIngredienteShouldNotBeFound("gr.equals=" + UPDATED_GR);
    }

    @Test
    @Transactional
    void getAllIngredientesByGrIsInShouldWork() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where gr in DEFAULT_GR or UPDATED_GR
        defaultIngredienteShouldBeFound("gr.in=" + DEFAULT_GR + "," + UPDATED_GR);

        // Get all the ingredienteList where gr equals to UPDATED_GR
        defaultIngredienteShouldNotBeFound("gr.in=" + UPDATED_GR);
    }

    @Test
    @Transactional
    void getAllIngredientesByGrIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where gr is not null
        defaultIngredienteShouldBeFound("gr.specified=true");

        // Get all the ingredienteList where gr is null
        defaultIngredienteShouldNotBeFound("gr.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientesByGrIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where gr is greater than or equal to DEFAULT_GR
        defaultIngredienteShouldBeFound("gr.greaterThanOrEqual=" + DEFAULT_GR);

        // Get all the ingredienteList where gr is greater than or equal to UPDATED_GR
        defaultIngredienteShouldNotBeFound("gr.greaterThanOrEqual=" + UPDATED_GR);
    }

    @Test
    @Transactional
    void getAllIngredientesByGrIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where gr is less than or equal to DEFAULT_GR
        defaultIngredienteShouldBeFound("gr.lessThanOrEqual=" + DEFAULT_GR);

        // Get all the ingredienteList where gr is less than or equal to SMALLER_GR
        defaultIngredienteShouldNotBeFound("gr.lessThanOrEqual=" + SMALLER_GR);
    }

    @Test
    @Transactional
    void getAllIngredientesByGrIsLessThanSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where gr is less than DEFAULT_GR
        defaultIngredienteShouldNotBeFound("gr.lessThan=" + DEFAULT_GR);

        // Get all the ingredienteList where gr is less than UPDATED_GR
        defaultIngredienteShouldBeFound("gr.lessThan=" + UPDATED_GR);
    }

    @Test
    @Transactional
    void getAllIngredientesByGrIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where gr is greater than DEFAULT_GR
        defaultIngredienteShouldNotBeFound("gr.greaterThan=" + DEFAULT_GR);

        // Get all the ingredienteList where gr is greater than SMALLER_GR
        defaultIngredienteShouldBeFound("gr.greaterThan=" + SMALLER_GR);
    }

    @Test
    @Transactional
    void getAllIngredientesByCalIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where cal equals to DEFAULT_CAL
        defaultIngredienteShouldBeFound("cal.equals=" + DEFAULT_CAL);

        // Get all the ingredienteList where cal equals to UPDATED_CAL
        defaultIngredienteShouldNotBeFound("cal.equals=" + UPDATED_CAL);
    }

    @Test
    @Transactional
    void getAllIngredientesByCalIsInShouldWork() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where cal in DEFAULT_CAL or UPDATED_CAL
        defaultIngredienteShouldBeFound("cal.in=" + DEFAULT_CAL + "," + UPDATED_CAL);

        // Get all the ingredienteList where cal equals to UPDATED_CAL
        defaultIngredienteShouldNotBeFound("cal.in=" + UPDATED_CAL);
    }

    @Test
    @Transactional
    void getAllIngredientesByCalIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where cal is not null
        defaultIngredienteShouldBeFound("cal.specified=true");

        // Get all the ingredienteList where cal is null
        defaultIngredienteShouldNotBeFound("cal.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientesByCalContainsSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where cal contains DEFAULT_CAL
        defaultIngredienteShouldBeFound("cal.contains=" + DEFAULT_CAL);

        // Get all the ingredienteList where cal contains UPDATED_CAL
        defaultIngredienteShouldNotBeFound("cal.contains=" + UPDATED_CAL);
    }

    @Test
    @Transactional
    void getAllIngredientesByCalNotContainsSomething() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList where cal does not contain DEFAULT_CAL
        defaultIngredienteShouldNotBeFound("cal.doesNotContain=" + DEFAULT_CAL);

        // Get all the ingredienteList where cal does not contain UPDATED_CAL
        defaultIngredienteShouldBeFound("cal.doesNotContain=" + UPDATED_CAL);
    }

    @Test
    @Transactional
    void getAllIngredientesByHeladosIsEqualToSomething() throws Exception {
        Helado helados;
        if (TestUtil.findAll(em, Helado.class).isEmpty()) {
            ingredienteRepository.saveAndFlush(ingrediente);
            helados = HeladoResourceIT.createEntity(em);
        } else {
            helados = TestUtil.findAll(em, Helado.class).get(0);
        }
        em.persist(helados);
        em.flush();
        ingrediente.addHelados(helados);
        ingredienteRepository.saveAndFlush(ingrediente);
        Long heladosId = helados.getId();

        // Get all the ingredienteList where helados equals to heladosId
        defaultIngredienteShouldBeFound("heladosId.equals=" + heladosId);

        // Get all the ingredienteList where helados equals to (heladosId + 1)
        defaultIngredienteShouldNotBeFound("heladosId.equals=" + (heladosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIngredienteShouldBeFound(String filter) throws Exception {
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingrediente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].gr").value(hasItem(DEFAULT_GR.doubleValue())))
            .andExpect(jsonPath("$.[*].cal").value(hasItem(DEFAULT_CAL)));

        // Check, that the count call also returns 1
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIngredienteShouldNotBeFound(String filter) throws Exception {
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIngrediente() throws Exception {
        // Get the ingrediente
        restIngredienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngrediente() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();

        // Update the ingrediente
        Ingrediente updatedIngrediente = ingredienteRepository.findById(ingrediente.getId()).get();
        // Disconnect from session so that the updates on updatedIngrediente are not directly saved in db
        em.detach(updatedIngrediente);
        updatedIngrediente.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).gr(UPDATED_GR).cal(UPDATED_CAL);
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(updatedIngrediente);

        restIngredienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testIngrediente.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testIngrediente.getGr()).isEqualTo(UPDATED_GR);
        assertThat(testIngrediente.getCal()).isEqualTo(UPDATED_CAL);
    }

    @Test
    @Transactional
    void putNonExistingIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // Create the Ingrediente
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // Create the Ingrediente
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // Create the Ingrediente
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngredienteWithPatch() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();

        // Update the ingrediente using partial update
        Ingrediente partialUpdatedIngrediente = new Ingrediente();
        partialUpdatedIngrediente.setId(ingrediente.getId());

        partialUpdatedIngrediente.nombre(UPDATED_NOMBRE);

        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngrediente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngrediente))
            )
            .andExpect(status().isOk());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testIngrediente.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testIngrediente.getGr()).isEqualTo(DEFAULT_GR);
        assertThat(testIngrediente.getCal()).isEqualTo(DEFAULT_CAL);
    }

    @Test
    @Transactional
    void fullUpdateIngredienteWithPatch() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();

        // Update the ingrediente using partial update
        Ingrediente partialUpdatedIngrediente = new Ingrediente();
        partialUpdatedIngrediente.setId(ingrediente.getId());

        partialUpdatedIngrediente.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).gr(UPDATED_GR).cal(UPDATED_CAL);

        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngrediente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngrediente))
            )
            .andExpect(status().isOk());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testIngrediente.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testIngrediente.getGr()).isEqualTo(UPDATED_GR);
        assertThat(testIngrediente.getCal()).isEqualTo(UPDATED_CAL);
    }

    @Test
    @Transactional
    void patchNonExistingIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // Create the Ingrediente
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingredienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // Create the Ingrediente
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // Create the Ingrediente
        IngredienteDTO ingredienteDTO = ingredienteMapper.toDto(ingrediente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ingredienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngrediente() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeDelete = ingredienteRepository.findAll().size();

        // Delete the ingrediente
        restIngredienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingrediente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
