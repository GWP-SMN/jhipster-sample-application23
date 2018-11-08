package com.openix.prueba23.web.rest;

import com.openix.prueba23.JhipsterSampleApplicationWithGradleApp;

import com.openix.prueba23.domain.Key;
import com.openix.prueba23.domain.Registry;
import com.openix.prueba23.domain.App;
import com.openix.prueba23.repository.KeyRepository;
import com.openix.prueba23.service.KeyService;
import com.openix.prueba23.service.dto.KeyDTO;
import com.openix.prueba23.service.mapper.KeyMapper;
import com.openix.prueba23.web.rest.errors.ExceptionTranslator;
import com.openix.prueba23.service.dto.KeyCriteria;
import com.openix.prueba23.service.KeyQueryService;

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
import java.util.List;


import static com.openix.prueba23.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the KeyResource REST controller.
 *
 * @see KeyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationWithGradleApp.class)
public class KeyResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_USED = false;
    private static final Boolean UPDATED_USED = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private KeyMapper keyMapper;

    @Autowired
    private KeyService keyService;

    @Autowired
    private KeyQueryService keyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKeyMockMvc;

    private Key key;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KeyResource keyResource = new KeyResource(keyService, keyQueryService);
        this.restKeyMockMvc = MockMvcBuilders.standaloneSetup(keyResource)
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
    public static Key createEntity(EntityManager em) {
        Key key = new Key()
            .code(DEFAULT_CODE)
            .used(DEFAULT_USED)
            .deleted(DEFAULT_DELETED);
        return key;
    }

    @Before
    public void initTest() {
        key = createEntity(em);
    }

    @Test
    @Transactional
    public void createKey() throws Exception {
        int databaseSizeBeforeCreate = keyRepository.findAll().size();

        // Create the Key
        KeyDTO keyDTO = keyMapper.toDto(key);
        restKeyMockMvc.perform(post("/api/keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyDTO)))
            .andExpect(status().isCreated());

        // Validate the Key in the database
        List<Key> keyList = keyRepository.findAll();
        assertThat(keyList).hasSize(databaseSizeBeforeCreate + 1);
        Key testKey = keyList.get(keyList.size() - 1);
        assertThat(testKey.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testKey.isUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testKey.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createKeyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keyRepository.findAll().size();

        // Create the Key with an existing ID
        key.setId(1L);
        KeyDTO keyDTO = keyMapper.toDto(key);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeyMockMvc.perform(post("/api/keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Key in the database
        List<Key> keyList = keyRepository.findAll();
        assertThat(keyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = keyRepository.findAll().size();
        // set the field null
        key.setCode(null);

        // Create the Key, which fails.
        KeyDTO keyDTO = keyMapper.toDto(key);

        restKeyMockMvc.perform(post("/api/keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyDTO)))
            .andExpect(status().isBadRequest());

        List<Key> keyList = keyRepository.findAll();
        assertThat(keyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKeys() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList
        restKeyMockMvc.perform(get("/api/keys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(key.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getKey() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get the key
        restKeyMockMvc.perform(get("/api/keys/{id}", key.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(key.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.used").value(DEFAULT_USED.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllKeysByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where code equals to DEFAULT_CODE
        defaultKeyShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the keyList where code equals to UPDATED_CODE
        defaultKeyShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllKeysByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where code in DEFAULT_CODE or UPDATED_CODE
        defaultKeyShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the keyList where code equals to UPDATED_CODE
        defaultKeyShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllKeysByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where code is not null
        defaultKeyShouldBeFound("code.specified=true");

        // Get all the keyList where code is null
        defaultKeyShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllKeysByUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where used equals to DEFAULT_USED
        defaultKeyShouldBeFound("used.equals=" + DEFAULT_USED);

        // Get all the keyList where used equals to UPDATED_USED
        defaultKeyShouldNotBeFound("used.equals=" + UPDATED_USED);
    }

    @Test
    @Transactional
    public void getAllKeysByUsedIsInShouldWork() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where used in DEFAULT_USED or UPDATED_USED
        defaultKeyShouldBeFound("used.in=" + DEFAULT_USED + "," + UPDATED_USED);

        // Get all the keyList where used equals to UPDATED_USED
        defaultKeyShouldNotBeFound("used.in=" + UPDATED_USED);
    }

    @Test
    @Transactional
    public void getAllKeysByUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where used is not null
        defaultKeyShouldBeFound("used.specified=true");

        // Get all the keyList where used is null
        defaultKeyShouldNotBeFound("used.specified=false");
    }

    @Test
    @Transactional
    public void getAllKeysByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where deleted equals to DEFAULT_DELETED
        defaultKeyShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the keyList where deleted equals to UPDATED_DELETED
        defaultKeyShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllKeysByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultKeyShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the keyList where deleted equals to UPDATED_DELETED
        defaultKeyShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllKeysByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        // Get all the keyList where deleted is not null
        defaultKeyShouldBeFound("deleted.specified=true");

        // Get all the keyList where deleted is null
        defaultKeyShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    public void getAllKeysByRegistryIsEqualToSomething() throws Exception {
        // Initialize the database
        Registry registry = RegistryResourceIntTest.createEntity(em);
        em.persist(registry);
        em.flush();
        key.setRegistry(registry);
        keyRepository.saveAndFlush(key);
        Long registryId = registry.getId();

        // Get all the keyList where registry equals to registryId
        defaultKeyShouldBeFound("registryId.equals=" + registryId);

        // Get all the keyList where registry equals to registryId + 1
        defaultKeyShouldNotBeFound("registryId.equals=" + (registryId + 1));
    }


    @Test
    @Transactional
    public void getAllKeysByAppIsEqualToSomething() throws Exception {
        // Initialize the database
        App app = AppResourceIntTest.createEntity(em);
        em.persist(app);
        em.flush();
        key.setApp(app);
        keyRepository.saveAndFlush(key);
        Long appId = app.getId();

        // Get all the keyList where app equals to appId
        defaultKeyShouldBeFound("appId.equals=" + appId);

        // Get all the keyList where app equals to appId + 1
        defaultKeyShouldNotBeFound("appId.equals=" + (appId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultKeyShouldBeFound(String filter) throws Exception {
        restKeyMockMvc.perform(get("/api/keys?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(key.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restKeyMockMvc.perform(get("/api/keys/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultKeyShouldNotBeFound(String filter) throws Exception {
        restKeyMockMvc.perform(get("/api/keys?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKeyMockMvc.perform(get("/api/keys/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingKey() throws Exception {
        // Get the key
        restKeyMockMvc.perform(get("/api/keys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKey() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        int databaseSizeBeforeUpdate = keyRepository.findAll().size();

        // Update the key
        Key updatedKey = keyRepository.findById(key.getId()).get();
        // Disconnect from session so that the updates on updatedKey are not directly saved in db
        em.detach(updatedKey);
        updatedKey
            .code(UPDATED_CODE)
            .used(UPDATED_USED)
            .deleted(UPDATED_DELETED);
        KeyDTO keyDTO = keyMapper.toDto(updatedKey);

        restKeyMockMvc.perform(put("/api/keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyDTO)))
            .andExpect(status().isOk());

        // Validate the Key in the database
        List<Key> keyList = keyRepository.findAll();
        assertThat(keyList).hasSize(databaseSizeBeforeUpdate);
        Key testKey = keyList.get(keyList.size() - 1);
        assertThat(testKey.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testKey.isUsed()).isEqualTo(UPDATED_USED);
        assertThat(testKey.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingKey() throws Exception {
        int databaseSizeBeforeUpdate = keyRepository.findAll().size();

        // Create the Key
        KeyDTO keyDTO = keyMapper.toDto(key);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeyMockMvc.perform(put("/api/keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Key in the database
        List<Key> keyList = keyRepository.findAll();
        assertThat(keyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKey() throws Exception {
        // Initialize the database
        keyRepository.saveAndFlush(key);

        int databaseSizeBeforeDelete = keyRepository.findAll().size();

        // Get the key
        restKeyMockMvc.perform(delete("/api/keys/{id}", key.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Key> keyList = keyRepository.findAll();
        assertThat(keyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Key.class);
        Key key1 = new Key();
        key1.setId(1L);
        Key key2 = new Key();
        key2.setId(key1.getId());
        assertThat(key1).isEqualTo(key2);
        key2.setId(2L);
        assertThat(key1).isNotEqualTo(key2);
        key1.setId(null);
        assertThat(key1).isNotEqualTo(key2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyDTO.class);
        KeyDTO keyDTO1 = new KeyDTO();
        keyDTO1.setId(1L);
        KeyDTO keyDTO2 = new KeyDTO();
        assertThat(keyDTO1).isNotEqualTo(keyDTO2);
        keyDTO2.setId(keyDTO1.getId());
        assertThat(keyDTO1).isEqualTo(keyDTO2);
        keyDTO2.setId(2L);
        assertThat(keyDTO1).isNotEqualTo(keyDTO2);
        keyDTO1.setId(null);
        assertThat(keyDTO1).isNotEqualTo(keyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(keyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(keyMapper.fromId(null)).isNull();
    }
}
