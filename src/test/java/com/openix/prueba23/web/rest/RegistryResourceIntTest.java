package com.openix.prueba23.web.rest;

import com.openix.prueba23.JhipsterSampleApplicationWithGradleApp;

import com.openix.prueba23.domain.Registry;
import com.openix.prueba23.domain.App;
import com.openix.prueba23.domain.Subcategory;
import com.openix.prueba23.repository.RegistryRepository;
import com.openix.prueba23.service.RegistryService;
import com.openix.prueba23.service.dto.RegistryDTO;
import com.openix.prueba23.service.mapper.RegistryMapper;
import com.openix.prueba23.web.rest.errors.ExceptionTranslator;
import com.openix.prueba23.service.dto.RegistryCriteria;
import com.openix.prueba23.service.RegistryQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.openix.prueba23.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RegistryResource REST controller.
 *
 * @see RegistryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationWithGradleApp.class)
public class RegistryResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS = "BBBBBBBBBB";

    private static final String DEFAULT_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_OCCUPATION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALIDATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RESET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RESET_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESET_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESET_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final Instant DEFAULT_ACTIVATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    private RegistryMapper registryMapper;

    @Autowired
    private RegistryService registryService;

    @Autowired
    private RegistryQueryService registryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistryMockMvc;

    private Registry registry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RegistryResource registryResource = new RegistryResource(registryService, registryQueryService);
        this.restRegistryMockMvc = MockMvcBuilders.standaloneSetup(registryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registry createEntity(EntityManager em) {
        Registry registry = new Registry()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .dni(DEFAULT_DNI)
            .birthdate(DEFAULT_BIRTHDATE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .business(DEFAULT_BUSINESS)
            .occupation(DEFAULT_OCCUPATION)
            .email(DEFAULT_EMAIL)
            .activationKey(DEFAULT_ACTIVATION_KEY)
            .validationCode(DEFAULT_VALIDATION_CODE)
            .resetKey(DEFAULT_RESET_KEY)
            .resetDate(DEFAULT_RESET_DATE)
            .activated(DEFAULT_ACTIVATED)
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .deleted(DEFAULT_DELETED);
        return registry;
    }

    @Before
    public void initTest() {
        registry = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistry() throws Exception {
        int databaseSizeBeforeCreate = registryRepository.findAll().size();

        // Create the Registry
        RegistryDTO registryDTO = registryMapper.toDto(registry);
        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryDTO)))
            .andExpect(status().isCreated());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeCreate + 1);
        Registry testRegistry = registryList.get(registryList.size() - 1);
        assertThat(testRegistry.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testRegistry.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testRegistry.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testRegistry.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testRegistry.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testRegistry.getBusiness()).isEqualTo(DEFAULT_BUSINESS);
        assertThat(testRegistry.getOccupation()).isEqualTo(DEFAULT_OCCUPATION);
        assertThat(testRegistry.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRegistry.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testRegistry.getValidationCode()).isEqualTo(DEFAULT_VALIDATION_CODE);
        assertThat(testRegistry.getResetKey()).isEqualTo(DEFAULT_RESET_KEY);
        assertThat(testRegistry.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
        assertThat(testRegistry.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testRegistry.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testRegistry.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createRegistryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registryRepository.findAll().size();

        // Create the Registry with an existing ID
        registry.setId(1L);
        RegistryDTO registryDTO = registryMapper.toDto(registry);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = registryRepository.findAll().size();
        // set the field null
        registry.setEmail(null);

        // Create the Registry, which fails.
        RegistryDTO registryDTO = registryMapper.toDto(registry);

        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryDTO)))
            .andExpect(status().isBadRequest());

        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegistries() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList
        restRegistryMockMvc.perform(get("/api/registries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registry.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].business").value(hasItem(DEFAULT_BUSINESS.toString())))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY.toString())))
            .andExpect(jsonPath("$.[*].validationCode").value(hasItem(DEFAULT_VALIDATION_CODE.toString())))
            .andExpect(jsonPath("$.[*].resetKey").value(hasItem(DEFAULT_RESET_KEY.toString())))
            .andExpect(jsonPath("$.[*].resetDate").value(hasItem(DEFAULT_RESET_DATE.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRegistry() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get the registry
        restRegistryMockMvc.perform(get("/api/registries/{id}", registry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registry.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.business").value(DEFAULT_BUSINESS.toString()))
            .andExpect(jsonPath("$.occupation").value(DEFAULT_OCCUPATION.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.activationKey").value(DEFAULT_ACTIVATION_KEY.toString()))
            .andExpect(jsonPath("$.validationCode").value(DEFAULT_VALIDATION_CODE.toString()))
            .andExpect(jsonPath("$.resetKey").value(DEFAULT_RESET_KEY.toString()))
            .andExpect(jsonPath("$.resetDate").value(DEFAULT_RESET_DATE.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.activationDate").value(DEFAULT_ACTIVATION_DATE.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllRegistriesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where firstName equals to DEFAULT_FIRST_NAME
        defaultRegistryShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the registryList where firstName equals to UPDATED_FIRST_NAME
        defaultRegistryShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegistriesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultRegistryShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the registryList where firstName equals to UPDATED_FIRST_NAME
        defaultRegistryShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegistriesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where firstName is not null
        defaultRegistryShouldBeFound("firstName.specified=true");

        // Get all the registryList where firstName is null
        defaultRegistryShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where lastName equals to DEFAULT_LAST_NAME
        defaultRegistryShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the registryList where lastName equals to UPDATED_LAST_NAME
        defaultRegistryShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegistriesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultRegistryShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the registryList where lastName equals to UPDATED_LAST_NAME
        defaultRegistryShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegistriesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where lastName is not null
        defaultRegistryShouldBeFound("lastName.specified=true");

        // Get all the registryList where lastName is null
        defaultRegistryShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where dni equals to DEFAULT_DNI
        defaultRegistryShouldBeFound("dni.equals=" + DEFAULT_DNI);

        // Get all the registryList where dni equals to UPDATED_DNI
        defaultRegistryShouldNotBeFound("dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    public void getAllRegistriesByDniIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where dni in DEFAULT_DNI or UPDATED_DNI
        defaultRegistryShouldBeFound("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI);

        // Get all the registryList where dni equals to UPDATED_DNI
        defaultRegistryShouldNotBeFound("dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    public void getAllRegistriesByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where dni is not null
        defaultRegistryShouldBeFound("dni.specified=true");

        // Get all the registryList where dni is null
        defaultRegistryShouldNotBeFound("dni.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByBirthdateIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where birthdate equals to DEFAULT_BIRTHDATE
        defaultRegistryShouldBeFound("birthdate.equals=" + DEFAULT_BIRTHDATE);

        // Get all the registryList where birthdate equals to UPDATED_BIRTHDATE
        defaultRegistryShouldNotBeFound("birthdate.equals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByBirthdateIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where birthdate in DEFAULT_BIRTHDATE or UPDATED_BIRTHDATE
        defaultRegistryShouldBeFound("birthdate.in=" + DEFAULT_BIRTHDATE + "," + UPDATED_BIRTHDATE);

        // Get all the registryList where birthdate equals to UPDATED_BIRTHDATE
        defaultRegistryShouldNotBeFound("birthdate.in=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByBirthdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where birthdate is not null
        defaultRegistryShouldBeFound("birthdate.specified=true");

        // Get all the registryList where birthdate is null
        defaultRegistryShouldNotBeFound("birthdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByBirthdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where birthdate greater than or equals to DEFAULT_BIRTHDATE
        defaultRegistryShouldBeFound("birthdate.greaterOrEqualThan=" + DEFAULT_BIRTHDATE);

        // Get all the registryList where birthdate greater than or equals to UPDATED_BIRTHDATE
        defaultRegistryShouldNotBeFound("birthdate.greaterOrEqualThan=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByBirthdateIsLessThanSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where birthdate less than or equals to DEFAULT_BIRTHDATE
        defaultRegistryShouldNotBeFound("birthdate.lessThan=" + DEFAULT_BIRTHDATE);

        // Get all the registryList where birthdate less than or equals to UPDATED_BIRTHDATE
        defaultRegistryShouldBeFound("birthdate.lessThan=" + UPDATED_BIRTHDATE);
    }


    @Test
    @Transactional
    public void getAllRegistriesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultRegistryShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the registryList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultRegistryShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRegistriesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultRegistryShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the registryList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultRegistryShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRegistriesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where phoneNumber is not null
        defaultRegistryShouldBeFound("phoneNumber.specified=true");

        // Get all the registryList where phoneNumber is null
        defaultRegistryShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByBusinessIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where business equals to DEFAULT_BUSINESS
        defaultRegistryShouldBeFound("business.equals=" + DEFAULT_BUSINESS);

        // Get all the registryList where business equals to UPDATED_BUSINESS
        defaultRegistryShouldNotBeFound("business.equals=" + UPDATED_BUSINESS);
    }

    @Test
    @Transactional
    public void getAllRegistriesByBusinessIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where business in DEFAULT_BUSINESS or UPDATED_BUSINESS
        defaultRegistryShouldBeFound("business.in=" + DEFAULT_BUSINESS + "," + UPDATED_BUSINESS);

        // Get all the registryList where business equals to UPDATED_BUSINESS
        defaultRegistryShouldNotBeFound("business.in=" + UPDATED_BUSINESS);
    }

    @Test
    @Transactional
    public void getAllRegistriesByBusinessIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where business is not null
        defaultRegistryShouldBeFound("business.specified=true");

        // Get all the registryList where business is null
        defaultRegistryShouldNotBeFound("business.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByOccupationIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where occupation equals to DEFAULT_OCCUPATION
        defaultRegistryShouldBeFound("occupation.equals=" + DEFAULT_OCCUPATION);

        // Get all the registryList where occupation equals to UPDATED_OCCUPATION
        defaultRegistryShouldNotBeFound("occupation.equals=" + UPDATED_OCCUPATION);
    }

    @Test
    @Transactional
    public void getAllRegistriesByOccupationIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where occupation in DEFAULT_OCCUPATION or UPDATED_OCCUPATION
        defaultRegistryShouldBeFound("occupation.in=" + DEFAULT_OCCUPATION + "," + UPDATED_OCCUPATION);

        // Get all the registryList where occupation equals to UPDATED_OCCUPATION
        defaultRegistryShouldNotBeFound("occupation.in=" + UPDATED_OCCUPATION);
    }

    @Test
    @Transactional
    public void getAllRegistriesByOccupationIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where occupation is not null
        defaultRegistryShouldBeFound("occupation.specified=true");

        // Get all the registryList where occupation is null
        defaultRegistryShouldNotBeFound("occupation.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where email equals to DEFAULT_EMAIL
        defaultRegistryShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the registryList where email equals to UPDATED_EMAIL
        defaultRegistryShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRegistriesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultRegistryShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the registryList where email equals to UPDATED_EMAIL
        defaultRegistryShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRegistriesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where email is not null
        defaultRegistryShouldBeFound("email.specified=true");

        // Get all the registryList where email is null
        defaultRegistryShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivationKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activationKey equals to DEFAULT_ACTIVATION_KEY
        defaultRegistryShouldBeFound("activationKey.equals=" + DEFAULT_ACTIVATION_KEY);

        // Get all the registryList where activationKey equals to UPDATED_ACTIVATION_KEY
        defaultRegistryShouldNotBeFound("activationKey.equals=" + UPDATED_ACTIVATION_KEY);
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivationKeyIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activationKey in DEFAULT_ACTIVATION_KEY or UPDATED_ACTIVATION_KEY
        defaultRegistryShouldBeFound("activationKey.in=" + DEFAULT_ACTIVATION_KEY + "," + UPDATED_ACTIVATION_KEY);

        // Get all the registryList where activationKey equals to UPDATED_ACTIVATION_KEY
        defaultRegistryShouldNotBeFound("activationKey.in=" + UPDATED_ACTIVATION_KEY);
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivationKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activationKey is not null
        defaultRegistryShouldBeFound("activationKey.specified=true");

        // Get all the registryList where activationKey is null
        defaultRegistryShouldNotBeFound("activationKey.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByValidationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where validationCode equals to DEFAULT_VALIDATION_CODE
        defaultRegistryShouldBeFound("validationCode.equals=" + DEFAULT_VALIDATION_CODE);

        // Get all the registryList where validationCode equals to UPDATED_VALIDATION_CODE
        defaultRegistryShouldNotBeFound("validationCode.equals=" + UPDATED_VALIDATION_CODE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByValidationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where validationCode in DEFAULT_VALIDATION_CODE or UPDATED_VALIDATION_CODE
        defaultRegistryShouldBeFound("validationCode.in=" + DEFAULT_VALIDATION_CODE + "," + UPDATED_VALIDATION_CODE);

        // Get all the registryList where validationCode equals to UPDATED_VALIDATION_CODE
        defaultRegistryShouldNotBeFound("validationCode.in=" + UPDATED_VALIDATION_CODE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByValidationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where validationCode is not null
        defaultRegistryShouldBeFound("validationCode.specified=true");

        // Get all the registryList where validationCode is null
        defaultRegistryShouldNotBeFound("validationCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByResetKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where resetKey equals to DEFAULT_RESET_KEY
        defaultRegistryShouldBeFound("resetKey.equals=" + DEFAULT_RESET_KEY);

        // Get all the registryList where resetKey equals to UPDATED_RESET_KEY
        defaultRegistryShouldNotBeFound("resetKey.equals=" + UPDATED_RESET_KEY);
    }

    @Test
    @Transactional
    public void getAllRegistriesByResetKeyIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where resetKey in DEFAULT_RESET_KEY or UPDATED_RESET_KEY
        defaultRegistryShouldBeFound("resetKey.in=" + DEFAULT_RESET_KEY + "," + UPDATED_RESET_KEY);

        // Get all the registryList where resetKey equals to UPDATED_RESET_KEY
        defaultRegistryShouldNotBeFound("resetKey.in=" + UPDATED_RESET_KEY);
    }

    @Test
    @Transactional
    public void getAllRegistriesByResetKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where resetKey is not null
        defaultRegistryShouldBeFound("resetKey.specified=true");

        // Get all the registryList where resetKey is null
        defaultRegistryShouldNotBeFound("resetKey.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByResetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where resetDate equals to DEFAULT_RESET_DATE
        defaultRegistryShouldBeFound("resetDate.equals=" + DEFAULT_RESET_DATE);

        // Get all the registryList where resetDate equals to UPDATED_RESET_DATE
        defaultRegistryShouldNotBeFound("resetDate.equals=" + UPDATED_RESET_DATE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByResetDateIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where resetDate in DEFAULT_RESET_DATE or UPDATED_RESET_DATE
        defaultRegistryShouldBeFound("resetDate.in=" + DEFAULT_RESET_DATE + "," + UPDATED_RESET_DATE);

        // Get all the registryList where resetDate equals to UPDATED_RESET_DATE
        defaultRegistryShouldNotBeFound("resetDate.in=" + UPDATED_RESET_DATE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByResetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where resetDate is not null
        defaultRegistryShouldBeFound("resetDate.specified=true");

        // Get all the registryList where resetDate is null
        defaultRegistryShouldNotBeFound("resetDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activated equals to DEFAULT_ACTIVATED
        defaultRegistryShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the registryList where activated equals to UPDATED_ACTIVATED
        defaultRegistryShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultRegistryShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the registryList where activated equals to UPDATED_ACTIVATED
        defaultRegistryShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activated is not null
        defaultRegistryShouldBeFound("activated.specified=true");

        // Get all the registryList where activated is null
        defaultRegistryShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activationDate equals to DEFAULT_ACTIVATION_DATE
        defaultRegistryShouldBeFound("activationDate.equals=" + DEFAULT_ACTIVATION_DATE);

        // Get all the registryList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultRegistryShouldNotBeFound("activationDate.equals=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivationDateIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activationDate in DEFAULT_ACTIVATION_DATE or UPDATED_ACTIVATION_DATE
        defaultRegistryShouldBeFound("activationDate.in=" + DEFAULT_ACTIVATION_DATE + "," + UPDATED_ACTIVATION_DATE);

        // Get all the registryList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultRegistryShouldNotBeFound("activationDate.in=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegistriesByActivationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where activationDate is not null
        defaultRegistryShouldBeFound("activationDate.specified=true");

        // Get all the registryList where activationDate is null
        defaultRegistryShouldNotBeFound("activationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where deleted equals to DEFAULT_DELETED
        defaultRegistryShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the registryList where deleted equals to UPDATED_DELETED
        defaultRegistryShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllRegistriesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultRegistryShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the registryList where deleted equals to UPDATED_DELETED
        defaultRegistryShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllRegistriesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList where deleted is not null
        defaultRegistryShouldBeFound("deleted.specified=true");

        // Get all the registryList where deleted is null
        defaultRegistryShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegistriesByAppIsEqualToSomething() throws Exception {
        // Initialize the database
        App app = AppResourceIntTest.createEntity(em);
        em.persist(app);
        em.flush();
        registry.setApp(app);
        registryRepository.saveAndFlush(registry);
        Long appId = app.getId();

        // Get all the registryList where app equals to appId
        defaultRegistryShouldBeFound("appId.equals=" + appId);

        // Get all the registryList where app equals to appId + 1
        defaultRegistryShouldNotBeFound("appId.equals=" + (appId + 1));
    }


    @Test
    @Transactional
    public void getAllRegistriesBySubcategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Subcategory subcategory = SubcategoryResourceIntTest.createEntity(em);
        em.persist(subcategory);
        em.flush();
        registry.setSubcategory(subcategory);
        registryRepository.saveAndFlush(registry);
        Long subcategoryId = subcategory.getId();

        // Get all the registryList where subcategory equals to subcategoryId
        defaultRegistryShouldBeFound("subcategoryId.equals=" + subcategoryId);

        // Get all the registryList where subcategory equals to subcategoryId + 1
        defaultRegistryShouldNotBeFound("subcategoryId.equals=" + (subcategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRegistryShouldBeFound(String filter) throws Exception {
        restRegistryMockMvc.perform(get("/api/registries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registry.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].business").value(hasItem(DEFAULT_BUSINESS.toString())))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY.toString())))
            .andExpect(jsonPath("$.[*].validationCode").value(hasItem(DEFAULT_VALIDATION_CODE.toString())))
            .andExpect(jsonPath("$.[*].resetKey").value(hasItem(DEFAULT_RESET_KEY.toString())))
            .andExpect(jsonPath("$.[*].resetDate").value(hasItem(DEFAULT_RESET_DATE.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restRegistryMockMvc.perform(get("/api/registries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRegistryShouldNotBeFound(String filter) throws Exception {
        restRegistryMockMvc.perform(get("/api/registries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegistryMockMvc.perform(get("/api/registries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRegistry() throws Exception {
        // Get the registry
        restRegistryMockMvc.perform(get("/api/registries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistry() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        int databaseSizeBeforeUpdate = registryRepository.findAll().size();

        // Update the registry
        Registry updatedRegistry = registryRepository.findById(registry.getId()).get();
        // Disconnect from session so that the updates on updatedRegistry are not directly saved in db
        em.detach(updatedRegistry);
        updatedRegistry
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dni(UPDATED_DNI)
            .birthdate(UPDATED_BIRTHDATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .business(UPDATED_BUSINESS)
            .occupation(UPDATED_OCCUPATION)
            .email(UPDATED_EMAIL)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .validationCode(UPDATED_VALIDATION_CODE)
            .resetKey(UPDATED_RESET_KEY)
            .resetDate(UPDATED_RESET_DATE)
            .activated(UPDATED_ACTIVATED)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .deleted(UPDATED_DELETED);
        RegistryDTO registryDTO = registryMapper.toDto(updatedRegistry);

        restRegistryMockMvc.perform(put("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryDTO)))
            .andExpect(status().isOk());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeUpdate);
        Registry testRegistry = registryList.get(registryList.size() - 1);
        assertThat(testRegistry.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testRegistry.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testRegistry.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testRegistry.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testRegistry.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testRegistry.getBusiness()).isEqualTo(UPDATED_BUSINESS);
        assertThat(testRegistry.getOccupation()).isEqualTo(UPDATED_OCCUPATION);
        assertThat(testRegistry.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRegistry.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testRegistry.getValidationCode()).isEqualTo(UPDATED_VALIDATION_CODE);
        assertThat(testRegistry.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testRegistry.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testRegistry.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testRegistry.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testRegistry.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistry() throws Exception {
        int databaseSizeBeforeUpdate = registryRepository.findAll().size();

        // Create the Registry
        RegistryDTO registryDTO = registryMapper.toDto(registry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistryMockMvc.perform(put("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegistry() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        int databaseSizeBeforeDelete = registryRepository.findAll().size();

        // Get the registry
        restRegistryMockMvc.perform(delete("/api/registries/{id}", registry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registry.class);
        Registry registry1 = new Registry();
        registry1.setId(1L);
        Registry registry2 = new Registry();
        registry2.setId(registry1.getId());
        assertThat(registry1).isEqualTo(registry2);
        registry2.setId(2L);
        assertThat(registry1).isNotEqualTo(registry2);
        registry1.setId(null);
        assertThat(registry1).isNotEqualTo(registry2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistryDTO.class);
        RegistryDTO registryDTO1 = new RegistryDTO();
        registryDTO1.setId(1L);
        RegistryDTO registryDTO2 = new RegistryDTO();
        assertThat(registryDTO1).isNotEqualTo(registryDTO2);
        registryDTO2.setId(registryDTO1.getId());
        assertThat(registryDTO1).isEqualTo(registryDTO2);
        registryDTO2.setId(2L);
        assertThat(registryDTO1).isNotEqualTo(registryDTO2);
        registryDTO1.setId(null);
        assertThat(registryDTO1).isNotEqualTo(registryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(registryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(registryMapper.fromId(null)).isNull();
    }
}
