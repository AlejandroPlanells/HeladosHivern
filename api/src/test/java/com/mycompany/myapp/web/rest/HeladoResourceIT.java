package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Fabricante;
import com.mycompany.myapp.domain.Helado;
import com.mycompany.myapp.domain.Ingrediente;
import com.mycompany.myapp.repository.HeladoRepository;
import com.mycompany.myapp.service.HeladoService;
import com.mycompany.myapp.service.criteria.HeladoCriteria;
import com.mycompany.myapp.service.dto.HeladoDTO;
import com.mycompany.myapp.service.mapper.HeladoMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HeladoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HeladoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EN_OFERTA = false;
    private static final Boolean UPDATED_EN_OFERTA = true;

    private static final Double DEFAULT_PRECIO_OFERTA = 1D;
    private static final Double UPDATED_PRECIO_OFERTA = 2D;
    private static final Double SMALLER_PRECIO_OFERTA = 1D - 1D;

    private static final Double DEFAULT_PRECIO = 1D;
    private static final Double UPDATED_PRECIO = 2D;
    private static final Double SMALLER_PRECIO = 1D - 1D;

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/helados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HeladoRepository heladoRepository;

    @Mock
    private HeladoRepository heladoRepositoryMock;

    @Autowired
    private HeladoMapper heladoMapper;

    @Mock
    private HeladoService heladoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHeladoMockMvc;

    private Helado helado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Helado createEntity(EntityManager em) {
        Helado helado = new Helado()
            .nombre(DEFAULT_NOMBRE)
            .enOferta(DEFAULT_EN_OFERTA)
            .precioOferta(DEFAULT_PRECIO_OFERTA)
            .precio(DEFAULT_PRECIO)
            .fechaCreacion(DEFAULT_FECHA_CREACION);
        // Add required entity
        Fabricante fabricante;
        if (TestUtil.findAll(em, Fabricante.class).isEmpty()) {
            fabricante = FabricanteResourceIT.createEntity(em);
            em.persist(fabricante);
            em.flush();
        } else {
            fabricante = TestUtil.findAll(em, Fabricante.class).get(0);
        }
        helado.setFabricante(fabricante);
        return helado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Helado createUpdatedEntity(EntityManager em) {
        Helado helado = new Helado()
            .nombre(UPDATED_NOMBRE)
            .enOferta(UPDATED_EN_OFERTA)
            .precioOferta(UPDATED_PRECIO_OFERTA)
            .precio(UPDATED_PRECIO)
            .fechaCreacion(UPDATED_FECHA_CREACION);
        // Add required entity
        Fabricante fabricante;
        if (TestUtil.findAll(em, Fabricante.class).isEmpty()) {
            fabricante = FabricanteResourceIT.createUpdatedEntity(em);
            em.persist(fabricante);
            em.flush();
        } else {
            fabricante = TestUtil.findAll(em, Fabricante.class).get(0);
        }
        helado.setFabricante(fabricante);
        return helado;
    }

    @BeforeEach
    public void initTest() {
        helado = createEntity(em);
    }

    @Test
    @Transactional
    void createHelado() throws Exception {
        int databaseSizeBeforeCreate = heladoRepository.findAll().size();
        // Create the Helado
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);
        restHeladoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(heladoDTO)))
            .andExpect(status().isCreated());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeCreate + 1);
        Helado testHelado = heladoList.get(heladoList.size() - 1);
        assertThat(testHelado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testHelado.getEnOferta()).isEqualTo(DEFAULT_EN_OFERTA);
        assertThat(testHelado.getPrecioOferta()).isEqualTo(DEFAULT_PRECIO_OFERTA);
        assertThat(testHelado.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testHelado.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
    }

    @Test
    @Transactional
    void createHeladoWithExistingId() throws Exception {
        // Create the Helado with an existing ID
        helado.setId(1L);
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        int databaseSizeBeforeCreate = heladoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeladoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(heladoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = heladoRepository.findAll().size();
        // set the field null
        helado.setNombre(null);

        // Create the Helado, which fails.
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        restHeladoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(heladoDTO)))
            .andExpect(status().isBadRequest());

        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnOfertaIsRequired() throws Exception {
        int databaseSizeBeforeTest = heladoRepository.findAll().size();
        // set the field null
        helado.setEnOferta(null);

        // Create the Helado, which fails.
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        restHeladoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(heladoDTO)))
            .andExpect(status().isBadRequest());

        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = heladoRepository.findAll().size();
        // set the field null
        helado.setPrecio(null);

        // Create the Helado, which fails.
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        restHeladoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(heladoDTO)))
            .andExpect(status().isBadRequest());

        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaCreacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = heladoRepository.findAll().size();
        // set the field null
        helado.setFechaCreacion(null);

        // Create the Helado, which fails.
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        restHeladoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(heladoDTO)))
            .andExpect(status().isBadRequest());

        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelados() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList
        restHeladoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].enOferta").value(hasItem(DEFAULT_EN_OFERTA.booleanValue())))
            .andExpect(jsonPath("$.[*].precioOferta").value(hasItem(DEFAULT_PRECIO_OFERTA.doubleValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHeladosWithEagerRelationshipsIsEnabled() throws Exception {
        when(heladoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHeladoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(heladoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHeladosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(heladoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHeladoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(heladoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHelado() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get the helado
        restHeladoMockMvc
            .perform(get(ENTITY_API_URL_ID, helado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.enOferta").value(DEFAULT_EN_OFERTA.booleanValue()))
            .andExpect(jsonPath("$.precioOferta").value(DEFAULT_PRECIO_OFERTA.doubleValue()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()));
    }

    @Test
    @Transactional
    void getHeladosByIdFiltering() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        Long id = helado.getId();

        defaultHeladoShouldBeFound("id.equals=" + id);
        defaultHeladoShouldNotBeFound("id.notEquals=" + id);

        defaultHeladoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHeladoShouldNotBeFound("id.greaterThan=" + id);

        defaultHeladoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHeladoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHeladosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where nombre equals to DEFAULT_NOMBRE
        defaultHeladoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the heladoList where nombre equals to UPDATED_NOMBRE
        defaultHeladoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllHeladosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultHeladoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the heladoList where nombre equals to UPDATED_NOMBRE
        defaultHeladoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllHeladosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where nombre is not null
        defaultHeladoShouldBeFound("nombre.specified=true");

        // Get all the heladoList where nombre is null
        defaultHeladoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllHeladosByNombreContainsSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where nombre contains DEFAULT_NOMBRE
        defaultHeladoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the heladoList where nombre contains UPDATED_NOMBRE
        defaultHeladoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllHeladosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where nombre does not contain DEFAULT_NOMBRE
        defaultHeladoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the heladoList where nombre does not contain UPDATED_NOMBRE
        defaultHeladoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllHeladosByEnOfertaIsEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where enOferta equals to DEFAULT_EN_OFERTA
        defaultHeladoShouldBeFound("enOferta.equals=" + DEFAULT_EN_OFERTA);

        // Get all the heladoList where enOferta equals to UPDATED_EN_OFERTA
        defaultHeladoShouldNotBeFound("enOferta.equals=" + UPDATED_EN_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByEnOfertaIsInShouldWork() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where enOferta in DEFAULT_EN_OFERTA or UPDATED_EN_OFERTA
        defaultHeladoShouldBeFound("enOferta.in=" + DEFAULT_EN_OFERTA + "," + UPDATED_EN_OFERTA);

        // Get all the heladoList where enOferta equals to UPDATED_EN_OFERTA
        defaultHeladoShouldNotBeFound("enOferta.in=" + UPDATED_EN_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByEnOfertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where enOferta is not null
        defaultHeladoShouldBeFound("enOferta.specified=true");

        // Get all the heladoList where enOferta is null
        defaultHeladoShouldNotBeFound("enOferta.specified=false");
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioOfertaIsEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precioOferta equals to DEFAULT_PRECIO_OFERTA
        defaultHeladoShouldBeFound("precioOferta.equals=" + DEFAULT_PRECIO_OFERTA);

        // Get all the heladoList where precioOferta equals to UPDATED_PRECIO_OFERTA
        defaultHeladoShouldNotBeFound("precioOferta.equals=" + UPDATED_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioOfertaIsInShouldWork() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precioOferta in DEFAULT_PRECIO_OFERTA or UPDATED_PRECIO_OFERTA
        defaultHeladoShouldBeFound("precioOferta.in=" + DEFAULT_PRECIO_OFERTA + "," + UPDATED_PRECIO_OFERTA);

        // Get all the heladoList where precioOferta equals to UPDATED_PRECIO_OFERTA
        defaultHeladoShouldNotBeFound("precioOferta.in=" + UPDATED_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioOfertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precioOferta is not null
        defaultHeladoShouldBeFound("precioOferta.specified=true");

        // Get all the heladoList where precioOferta is null
        defaultHeladoShouldNotBeFound("precioOferta.specified=false");
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioOfertaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precioOferta is greater than or equal to DEFAULT_PRECIO_OFERTA
        defaultHeladoShouldBeFound("precioOferta.greaterThanOrEqual=" + DEFAULT_PRECIO_OFERTA);

        // Get all the heladoList where precioOferta is greater than or equal to UPDATED_PRECIO_OFERTA
        defaultHeladoShouldNotBeFound("precioOferta.greaterThanOrEqual=" + UPDATED_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioOfertaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precioOferta is less than or equal to DEFAULT_PRECIO_OFERTA
        defaultHeladoShouldBeFound("precioOferta.lessThanOrEqual=" + DEFAULT_PRECIO_OFERTA);

        // Get all the heladoList where precioOferta is less than or equal to SMALLER_PRECIO_OFERTA
        defaultHeladoShouldNotBeFound("precioOferta.lessThanOrEqual=" + SMALLER_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioOfertaIsLessThanSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precioOferta is less than DEFAULT_PRECIO_OFERTA
        defaultHeladoShouldNotBeFound("precioOferta.lessThan=" + DEFAULT_PRECIO_OFERTA);

        // Get all the heladoList where precioOferta is less than UPDATED_PRECIO_OFERTA
        defaultHeladoShouldBeFound("precioOferta.lessThan=" + UPDATED_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioOfertaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precioOferta is greater than DEFAULT_PRECIO_OFERTA
        defaultHeladoShouldNotBeFound("precioOferta.greaterThan=" + DEFAULT_PRECIO_OFERTA);

        // Get all the heladoList where precioOferta is greater than SMALLER_PRECIO_OFERTA
        defaultHeladoShouldBeFound("precioOferta.greaterThan=" + SMALLER_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioIsEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precio equals to DEFAULT_PRECIO
        defaultHeladoShouldBeFound("precio.equals=" + DEFAULT_PRECIO);

        // Get all the heladoList where precio equals to UPDATED_PRECIO
        defaultHeladoShouldNotBeFound("precio.equals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioIsInShouldWork() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precio in DEFAULT_PRECIO or UPDATED_PRECIO
        defaultHeladoShouldBeFound("precio.in=" + DEFAULT_PRECIO + "," + UPDATED_PRECIO);

        // Get all the heladoList where precio equals to UPDATED_PRECIO
        defaultHeladoShouldNotBeFound("precio.in=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioIsNullOrNotNull() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precio is not null
        defaultHeladoShouldBeFound("precio.specified=true");

        // Get all the heladoList where precio is null
        defaultHeladoShouldNotBeFound("precio.specified=false");
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precio is greater than or equal to DEFAULT_PRECIO
        defaultHeladoShouldBeFound("precio.greaterThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the heladoList where precio is greater than or equal to UPDATED_PRECIO
        defaultHeladoShouldNotBeFound("precio.greaterThanOrEqual=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precio is less than or equal to DEFAULT_PRECIO
        defaultHeladoShouldBeFound("precio.lessThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the heladoList where precio is less than or equal to SMALLER_PRECIO
        defaultHeladoShouldNotBeFound("precio.lessThanOrEqual=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioIsLessThanSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precio is less than DEFAULT_PRECIO
        defaultHeladoShouldNotBeFound("precio.lessThan=" + DEFAULT_PRECIO);

        // Get all the heladoList where precio is less than UPDATED_PRECIO
        defaultHeladoShouldBeFound("precio.lessThan=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllHeladosByPrecioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where precio is greater than DEFAULT_PRECIO
        defaultHeladoShouldNotBeFound("precio.greaterThan=" + DEFAULT_PRECIO);

        // Get all the heladoList where precio is greater than SMALLER_PRECIO
        defaultHeladoShouldBeFound("precio.greaterThan=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllHeladosByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where fechaCreacion equals to DEFAULT_FECHA_CREACION
        defaultHeladoShouldBeFound("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION);

        // Get all the heladoList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultHeladoShouldNotBeFound("fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllHeladosByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where fechaCreacion in DEFAULT_FECHA_CREACION or UPDATED_FECHA_CREACION
        defaultHeladoShouldBeFound("fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION);

        // Get all the heladoList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultHeladoShouldNotBeFound("fechaCreacion.in=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllHeladosByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        // Get all the heladoList where fechaCreacion is not null
        defaultHeladoShouldBeFound("fechaCreacion.specified=true");

        // Get all the heladoList where fechaCreacion is null
        defaultHeladoShouldNotBeFound("fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    void getAllHeladosByFabricanteIsEqualToSomething() throws Exception {
        Fabricante fabricante;
        if (TestUtil.findAll(em, Fabricante.class).isEmpty()) {
            heladoRepository.saveAndFlush(helado);
            fabricante = FabricanteResourceIT.createEntity(em);
        } else {
            fabricante = TestUtil.findAll(em, Fabricante.class).get(0);
        }
        em.persist(fabricante);
        em.flush();
        helado.setFabricante(fabricante);
        heladoRepository.saveAndFlush(helado);
        Long fabricanteId = fabricante.getId();

        // Get all the heladoList where fabricante equals to fabricanteId
        defaultHeladoShouldBeFound("fabricanteId.equals=" + fabricanteId);

        // Get all the heladoList where fabricante equals to (fabricanteId + 1)
        defaultHeladoShouldNotBeFound("fabricanteId.equals=" + (fabricanteId + 1));
    }

    @Test
    @Transactional
    void getAllHeladosByIngredientesIsEqualToSomething() throws Exception {
        Ingrediente ingredientes;
        if (TestUtil.findAll(em, Ingrediente.class).isEmpty()) {
            heladoRepository.saveAndFlush(helado);
            ingredientes = IngredienteResourceIT.createEntity(em);
        } else {
            ingredientes = TestUtil.findAll(em, Ingrediente.class).get(0);
        }
        em.persist(ingredientes);
        em.flush();
        helado.addIngredientes(ingredientes);
        heladoRepository.saveAndFlush(helado);
        Long ingredientesId = ingredientes.getId();

        // Get all the heladoList where ingredientes equals to ingredientesId
        defaultHeladoShouldBeFound("ingredientesId.equals=" + ingredientesId);

        // Get all the heladoList where ingredientes equals to (ingredientesId + 1)
        defaultHeladoShouldNotBeFound("ingredientesId.equals=" + (ingredientesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHeladoShouldBeFound(String filter) throws Exception {
        restHeladoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].enOferta").value(hasItem(DEFAULT_EN_OFERTA.booleanValue())))
            .andExpect(jsonPath("$.[*].precioOferta").value(hasItem(DEFAULT_PRECIO_OFERTA.doubleValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())));

        // Check, that the count call also returns 1
        restHeladoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHeladoShouldNotBeFound(String filter) throws Exception {
        restHeladoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHeladoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHelado() throws Exception {
        // Get the helado
        restHeladoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelado() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();

        // Update the helado
        Helado updatedHelado = heladoRepository.findById(helado.getId()).get();
        // Disconnect from session so that the updates on updatedHelado are not directly saved in db
        em.detach(updatedHelado);
        updatedHelado
            .nombre(UPDATED_NOMBRE)
            .enOferta(UPDATED_EN_OFERTA)
            .precioOferta(UPDATED_PRECIO_OFERTA)
            .precio(UPDATED_PRECIO)
            .fechaCreacion(UPDATED_FECHA_CREACION);
        HeladoDTO heladoDTO = heladoMapper.toDto(updatedHelado);

        restHeladoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, heladoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(heladoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
        Helado testHelado = heladoList.get(heladoList.size() - 1);
        assertThat(testHelado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHelado.getEnOferta()).isEqualTo(UPDATED_EN_OFERTA);
        assertThat(testHelado.getPrecioOferta()).isEqualTo(UPDATED_PRECIO_OFERTA);
        assertThat(testHelado.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testHelado.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void putNonExistingHelado() throws Exception {
        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();
        helado.setId(count.incrementAndGet());

        // Create the Helado
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeladoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, heladoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(heladoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelado() throws Exception {
        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();
        helado.setId(count.incrementAndGet());

        // Create the Helado
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeladoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(heladoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelado() throws Exception {
        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();
        helado.setId(count.incrementAndGet());

        // Create the Helado
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeladoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(heladoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHeladoWithPatch() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();

        // Update the helado using partial update
        Helado partialUpdatedHelado = new Helado();
        partialUpdatedHelado.setId(helado.getId());

        partialUpdatedHelado.enOferta(UPDATED_EN_OFERTA).precioOferta(UPDATED_PRECIO_OFERTA).fechaCreacion(UPDATED_FECHA_CREACION);

        restHeladoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelado))
            )
            .andExpect(status().isOk());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
        Helado testHelado = heladoList.get(heladoList.size() - 1);
        assertThat(testHelado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testHelado.getEnOferta()).isEqualTo(UPDATED_EN_OFERTA);
        assertThat(testHelado.getPrecioOferta()).isEqualTo(UPDATED_PRECIO_OFERTA);
        assertThat(testHelado.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testHelado.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void fullUpdateHeladoWithPatch() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();

        // Update the helado using partial update
        Helado partialUpdatedHelado = new Helado();
        partialUpdatedHelado.setId(helado.getId());

        partialUpdatedHelado
            .nombre(UPDATED_NOMBRE)
            .enOferta(UPDATED_EN_OFERTA)
            .precioOferta(UPDATED_PRECIO_OFERTA)
            .precio(UPDATED_PRECIO)
            .fechaCreacion(UPDATED_FECHA_CREACION);

        restHeladoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelado))
            )
            .andExpect(status().isOk());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
        Helado testHelado = heladoList.get(heladoList.size() - 1);
        assertThat(testHelado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHelado.getEnOferta()).isEqualTo(UPDATED_EN_OFERTA);
        assertThat(testHelado.getPrecioOferta()).isEqualTo(UPDATED_PRECIO_OFERTA);
        assertThat(testHelado.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testHelado.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void patchNonExistingHelado() throws Exception {
        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();
        helado.setId(count.incrementAndGet());

        // Create the Helado
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeladoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, heladoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(heladoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelado() throws Exception {
        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();
        helado.setId(count.incrementAndGet());

        // Create the Helado
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeladoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(heladoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelado() throws Exception {
        int databaseSizeBeforeUpdate = heladoRepository.findAll().size();
        helado.setId(count.incrementAndGet());

        // Create the Helado
        HeladoDTO heladoDTO = heladoMapper.toDto(helado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeladoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(heladoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Helado in the database
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelado() throws Exception {
        // Initialize the database
        heladoRepository.saveAndFlush(helado);

        int databaseSizeBeforeDelete = heladoRepository.findAll().size();

        // Delete the helado
        restHeladoMockMvc
            .perform(delete(ENTITY_API_URL_ID, helado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Helado> heladoList = heladoRepository.findAll();
        assertThat(heladoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
