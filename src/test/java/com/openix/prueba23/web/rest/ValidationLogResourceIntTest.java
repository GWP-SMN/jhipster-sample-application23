package com.openix.prueba23.web.rest;

import com.openix.prueba23.JhipsterSampleApplicationWithGradleApp;

import com.openix.prueba23.domain.ValidationLog;
import com.openix.prueba23.domain.Registry;
import com.openix.prueba23.repository.ValidationLogRepository;
import com.openix.prueba23.service.ValidationLogService;
import com.openix.prueba23.service.dto.ValidationLogDTO;
import com.openix.prueba23.service.mapper.ValidationLogMapper;
import com.openix.prueba23.web.rest.errors.ExceptionTranslator;
import com.openix.prueba23.service.dto.ValidationLogCriteria;
import com.openix.prueba23.service.ValidationLogQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.openix.prueba23.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ValidationLogResource REST controller.
 *
 * @see ValidationLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationWithGradleApp.class)
public class ValidationLogResourceIntTest {

    private static final Instant DEFAULT_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;

    private static final Boolean DEFAULT_SUCCESS = false;
    private static final Boolean UPDATED_SUCCESS = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private ValidationLogRepository validationLogRepository;

    @Autowired
    private ValidationLogMapper validationLogMapper;

    @Autowired
    private ValidationLogService validationLogService;

    @Autowired
    private ValidationLogQueryService validationLogQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restValidationLogMockMvc;

    private ValidationLog validationLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ValidationLogResource validationLogResource = new ValidationLogResource(validationLogService, validationLogQueryService);
        this.restValidationLogMockMvc = MockMvcBuilders.standaloneSetup(validationLogResource)
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
    public static ValidationLog createEntity(EntityManager em) {
        ValidationLog validationLog = new ValidationLog()
            .dateTime(DEFAULT_DATE_TIME)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .success(DEFAULT_SUCCESS)
            .deleted(DEFAULT_DELETED);
        return validationLog;
    }

    @Before
    public void initTest() {
        validationLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createValidationLog() throws Exception {
        int databaseSizeBeforeCreate = validationLogRepository.findAll().size();

        // Create the ValidationLog
        ValidationLogDTO validationLogDTO = validationLogMapper.toDto(validationLog);
        restValidationLogMockMvc.perform(post("/api/validation-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(validationLogDTO)))
            .andExpect(status().isCreated());

        // Validate the ValidationLog in the database
        List<ValidationLog> validationLogList = validationLogRepository.findAll();
        assertThat(validationLogList).hasSize(databaseSizeBeforeCreate + 1);
        ValidationLog testValidationLog = validationLogList.get(validationLogList.size() - 1);
        assertThat(testValidationLog.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testValidationLog.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testValidationLog.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testValidationLog.isSuccess()).isEqualTo(DEFAULT_SUCCESS);
        assertThat(testValidationLog.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createValidationLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = validationLogRepository.findAll().size();

        // Create the ValidationLog with an existing ID
        validationLog.setId(1L);
        ValidationLogDTO validationLogDTO = validationLogMapper.toDto(validationLog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValidationLogMockMvc.perform(post("/api/validation-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(validationLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ValidationLog in the database
        List<ValidationLog> validationLogList = validationLogRepository.findAll();
        assertThat(validationLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllValidationLogs() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList
        restValidationLogMockMvc.perform(get("/api/validation-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getValidationLog() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get the validationLog
        restValidationLogMockMvc.perform(get("/api/validation-logs/{id}", validationLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(validationLog.getId().intValue()))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.success").value(DEFAULT_SUCCESS.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllValidationLogsByDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where dateTime equals to DEFAULT_DATE_TIME
        defaultValidationLogShouldBeFound("dateTime.equals=" + DEFAULT_DATE_TIME);

        // Get all the validationLogList where dateTime equals to UPDATED_DATE_TIME
        defaultValidationLogShouldNotBeFound("dateTime.equals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where dateTime in DEFAULT_DATE_TIME or UPDATED_DATE_TIME
        defaultValidationLogShouldBeFound("dateTime.in=" + DEFAULT_DATE_TIME + "," + UPDATED_DATE_TIME);

        // Get all the validationLogList where dateTime equals to UPDATED_DATE_TIME
        defaultValidationLogShouldNotBeFound("dateTime.in=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where dateTime is not null
        defaultValidationLogShouldBeFound("dateTime.specified=true");

        // Get all the validationLogList where dateTime is null
        defaultValidationLogShouldNotBeFound("dateTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllValidationLogsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where latitude equals to DEFAULT_LATITUDE
        defaultValidationLogShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the validationLogList where latitude equals to UPDATED_LATITUDE
        defaultValidationLogShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultValidationLogShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the validationLogList where latitude equals to UPDATED_LATITUDE
        defaultValidationLogShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where latitude is not null
        defaultValidationLogShouldBeFound("latitude.specified=true");

        // Get all the validationLogList where latitude is null
        defaultValidationLogShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllValidationLogsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where longitude equals to DEFAULT_LONGITUDE
        defaultValidationLogShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the validationLogList where longitude equals to UPDATED_LONGITUDE
        defaultValidationLogShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultValidationLogShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the validationLogList where longitude equals to UPDATED_LONGITUDE
        defaultValidationLogShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where longitude is not null
        defaultValidationLogShouldBeFound("longitude.specified=true");

        // Get all the validationLogList where longitude is null
        defaultValidationLogShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllValidationLogsBySuccessIsEqualToSomething() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where success equals to DEFAULT_SUCCESS
        defaultValidationLogShouldBeFound("success.equals=" + DEFAULT_SUCCESS);

        // Get all the validationLogList where success equals to UPDATED_SUCCESS
        defaultValidationLogShouldNotBeFound("success.equals=" + UPDATED_SUCCESS);
    }

    @Test
    @Transactional
    public void getAllValidationLogsBySuccessIsInShouldWork() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where success in DEFAULT_SUCCESS or UPDATED_SUCCESS
        defaultValidationLogShouldBeFound("success.in=" + DEFAULT_SUCCESS + "," + UPDATED_SUCCESS);

        // Get all the validationLogList where success equals to UPDATED_SUCCESS
        defaultValidationLogShouldNotBeFound("success.in=" + UPDATED_SUCCESS);
    }

    @Test
    @Transactional
    public void getAllValidationLogsBySuccessIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where success is not null
        defaultValidationLogShouldBeFound("success.specified=true");

        // Get all the validationLogList where success is null
        defaultValidationLogShouldNotBeFound("success.specified=false");
    }

    @Test
    @Transactional
    public void getAllValidationLogsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where deleted equals to DEFAULT_DELETED
        defaultValidationLogShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the validationLogList where deleted equals to UPDATED_DELETED
        defaultValidationLogShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultValidationLogShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the validationLogList where deleted equals to UPDATED_DELETED
        defaultValidationLogShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllValidationLogsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        // Get all the validationLogList where deleted is not null
        defaultValidationLogShouldBeFound("deleted.specified=true");

        // Get all the validationLogList where deleted is null
        defaultValidationLogShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    public void getAllValidationLogsByRegistryIsEqualToSomething() throws Exception {
        // Initialize the database
        Registry registry = RegistryResourceIntTest.createEntity(em);
        em.persist(registry);
        em.flush();
        validationLog.setRegistry(registry);
        validationLogRepository.saveAndFlush(validationLog);
        Long registryId = registry.getId();

        // Get all the validationLogList where registry equals to registryId
        defaultValidationLogShouldBeFound("registryId.equals=" + registryId);

        // Get all the validationLogList where registry equals to registryId + 1
        defaultValidationLogShouldNotBeFound("registryId.equals=" + (registryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultValidationLogShouldBeFound(String filter) throws Exception {
        restValidationLogMockMvc.perform(get("/api/validation-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restValidationLogMockMvc.perform(get("/api/validation-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultValidationLogShouldNotBeFound(String filter) throws Exception {
        restValidationLogMockMvc.perform(get("/api/validation-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restValidationLogMockMvc.perform(get("/api/validation-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingValidationLog() throws Exception {
        // Get the validationLog
        restValidationLogMockMvc.perform(get("/api/validation-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValidationLog() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        int databaseSizeBeforeUpdate = validationLogRepository.findAll().size();

        // Update the validationLog
        ValidationLog updatedValidationLog = validationLogRepository.findById(validationLog.getId()).get();
        // Disconnect from session so that the updates on updatedValidationLog are not directly saved in db
        em.detach(updatedValidationLog);
        updatedValidationLog
            .dateTime(UPDATED_DATE_TIME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .success(UPDATED_SUCCESS)
            .deleted(UPDATED_DELETED);
        ValidationLogDTO validationLogDTO = validationLogMapper.toDto(updatedValidationLog);

        restValidationLogMockMvc.perform(put("/api/validation-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(validationLogDTO)))
            .andExpect(status().isOk());

        // Validate the ValidationLog in the database
        List<ValidationLog> validationLogList = validationLogRepository.findAll();
        assertThat(validationLogList).hasSize(databaseSizeBeforeUpdate);
        ValidationLog testValidationLog = validationLogList.get(validationLogList.size() - 1);
        assertThat(testValidationLog.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testValidationLog.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testValidationLog.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testValidationLog.isSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testValidationLog.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingValidationLog() throws Exception {
        int databaseSizeBeforeUpdate = validationLogRepository.findAll().size();

        // Create the ValidationLog
        ValidationLogDTO validationLogDTO = validationLogMapper.toDto(validationLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationLogMockMvc.perform(put("/api/validation-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(validationLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ValidationLog in the database
        List<ValidationLog> validationLogList = validationLogRepository.findAll();
        assertThat(validationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteValidationLog() throws Exception {
        // Initialize the database
        validationLogRepository.saveAndFlush(validationLog);

        int databaseSizeBeforeDelete = validationLogRepository.findAll().size();

        // Get the validationLog
        restValidationLogMockMvc.perform(delete("/api/validation-logs/{id}", validationLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ValidationLog> validationLogList = validationLogRepository.findAll();
        assertThat(validationLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationLog.class);
        ValidationLog validationLog1 = new ValidationLog();
        validationLog1.setId(1L);
        ValidationLog validationLog2 = new ValidationLog();
        validationLog2.setId(validationLog1.getId());
        assertThat(validationLog1).isEqualTo(validationLog2);
        validationLog2.setId(2L);
        assertThat(validationLog1).isNotEqualTo(validationLog2);
        validationLog1.setId(null);
        assertThat(validationLog1).isNotEqualTo(validationLog2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationLogDTO.class);
        ValidationLogDTO validationLogDTO1 = new ValidationLogDTO();
        validationLogDTO1.setId(1L);
        ValidationLogDTO validationLogDTO2 = new ValidationLogDTO();
        assertThat(validationLogDTO1).isNotEqualTo(validationLogDTO2);
        validationLogDTO2.setId(validationLogDTO1.getId());
        assertThat(validationLogDTO1).isEqualTo(validationLogDTO2);
        validationLogDTO2.setId(2L);
        assertThat(validationLogDTO1).isNotEqualTo(validationLogDTO2);
        validationLogDTO1.setId(null);
        assertThat(validationLogDTO1).isNotEqualTo(validationLogDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(validationLogMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(validationLogMapper.fromId(null)).isNull();
    }
}
