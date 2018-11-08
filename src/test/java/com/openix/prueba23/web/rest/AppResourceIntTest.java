package com.openix.prueba23.web.rest;

import com.openix.prueba23.JhipsterSampleApplicationWithGradleApp;

import com.openix.prueba23.domain.App;
import com.openix.prueba23.domain.Subcategory;
import com.openix.prueba23.repository.AppRepository;
import com.openix.prueba23.service.AppService;
import com.openix.prueba23.service.dto.AppDTO;
import com.openix.prueba23.service.mapper.AppMapper;
import com.openix.prueba23.web.rest.errors.ExceptionTranslator;
import com.openix.prueba23.service.dto.AppCriteria;
import com.openix.prueba23.service.AppQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static com.openix.prueba23.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AppResource REST controller.
 *
 * @see AppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationWithGradleApp.class)
public class AppResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_BLOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_BLOB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private AppService appService;

    @Autowired
    private AppQueryService appQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppMockMvc;

    private App app;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppResource appResource = new AppResource(appService, appQueryService);
        this.restAppMockMvc = MockMvcBuilders.standaloneSetup(appResource)
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
    public static App createEntity(EntityManager em) {
        App app = new App()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .version(DEFAULT_VERSION)
            .imageBlob(DEFAULT_IMAGE_BLOB)
            .imageBlobContentType(DEFAULT_IMAGE_BLOB_CONTENT_TYPE)
            .image(DEFAULT_IMAGE)
            .activated(DEFAULT_ACTIVATED)
            .deleted(DEFAULT_DELETED);
        return app;
    }

    @Before
    public void initTest() {
        app = createEntity(em);
    }

    @Test
    @Transactional
    public void createApp() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().size();

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);
        restAppMockMvc.perform(post("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isCreated());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate + 1);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApp.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApp.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testApp.getImageBlob()).isEqualTo(DEFAULT_IMAGE_BLOB);
        assertThat(testApp.getImageBlobContentType()).isEqualTo(DEFAULT_IMAGE_BLOB_CONTENT_TYPE);
        assertThat(testApp.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testApp.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testApp.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().size();

        // Create the App with an existing ID
        app.setId(1L);
        AppDTO appDTO = appMapper.toDto(app);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppMockMvc.perform(post("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setName(null);

        // Create the App, which fails.
        AppDTO appDTO = appMapper.toDto(app);

        restAppMockMvc.perform(post("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApps() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList
        restAppMockMvc.perform(get("/api/apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].imageBlobContentType").value(hasItem(DEFAULT_IMAGE_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB))))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get the app
        restAppMockMvc.perform(get("/api/apps/{id}", app.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(app.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.imageBlobContentType").value(DEFAULT_IMAGE_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageBlob").value(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB)))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllAppsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where name equals to DEFAULT_NAME
        defaultAppShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the appList where name equals to UPDATED_NAME
        defaultAppShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAppsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAppShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the appList where name equals to UPDATED_NAME
        defaultAppShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAppsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where name is not null
        defaultAppShouldBeFound("name.specified=true");

        // Get all the appList where name is null
        defaultAppShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where description equals to DEFAULT_DESCRIPTION
        defaultAppShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the appList where description equals to UPDATED_DESCRIPTION
        defaultAppShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAppsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAppShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the appList where description equals to UPDATED_DESCRIPTION
        defaultAppShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAppsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where description is not null
        defaultAppShouldBeFound("description.specified=true");

        // Get all the appList where description is null
        defaultAppShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where version equals to DEFAULT_VERSION
        defaultAppShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the appList where version equals to UPDATED_VERSION
        defaultAppShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllAppsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultAppShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the appList where version equals to UPDATED_VERSION
        defaultAppShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllAppsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where version is not null
        defaultAppShouldBeFound("version.specified=true");

        // Get all the appList where version is null
        defaultAppShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where image equals to DEFAULT_IMAGE
        defaultAppShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the appList where image equals to UPDATED_IMAGE
        defaultAppShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAppsByImageIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultAppShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the appList where image equals to UPDATED_IMAGE
        defaultAppShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAppsByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where image is not null
        defaultAppShouldBeFound("image.specified=true");

        // Get all the appList where image is null
        defaultAppShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppsByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where activated equals to DEFAULT_ACTIVATED
        defaultAppShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the appList where activated equals to UPDATED_ACTIVATED
        defaultAppShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllAppsByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultAppShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the appList where activated equals to UPDATED_ACTIVATED
        defaultAppShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllAppsByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where activated is not null
        defaultAppShouldBeFound("activated.specified=true");

        // Get all the appList where activated is null
        defaultAppShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where deleted equals to DEFAULT_DELETED
        defaultAppShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the appList where deleted equals to UPDATED_DELETED
        defaultAppShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllAppsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultAppShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the appList where deleted equals to UPDATED_DELETED
        defaultAppShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllAppsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where deleted is not null
        defaultAppShouldBeFound("deleted.specified=true");

        // Get all the appList where deleted is null
        defaultAppShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppsBySubcategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Subcategory subcategory = SubcategoryResourceIntTest.createEntity(em);
        em.persist(subcategory);
        em.flush();
        app.setSubcategory(subcategory);
        appRepository.saveAndFlush(app);
        Long subcategoryId = subcategory.getId();

        // Get all the appList where subcategory equals to subcategoryId
        defaultAppShouldBeFound("subcategoryId.equals=" + subcategoryId);

        // Get all the appList where subcategory equals to subcategoryId + 1
        defaultAppShouldNotBeFound("subcategoryId.equals=" + (subcategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAppShouldBeFound(String filter) throws Exception {
        restAppMockMvc.perform(get("/api/apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].imageBlobContentType").value(hasItem(DEFAULT_IMAGE_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB))))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restAppMockMvc.perform(get("/api/apps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAppShouldNotBeFound(String filter) throws Exception {
        restAppMockMvc.perform(get("/api/apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppMockMvc.perform(get("/api/apps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingApp() throws Exception {
        // Get the app
        restAppMockMvc.perform(get("/api/apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app
        App updatedApp = appRepository.findById(app.getId()).get();
        // Disconnect from session so that the updates on updatedApp are not directly saved in db
        em.detach(updatedApp);
        updatedApp
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION)
            .imageBlob(UPDATED_IMAGE_BLOB)
            .imageBlobContentType(UPDATED_IMAGE_BLOB_CONTENT_TYPE)
            .image(UPDATED_IMAGE)
            .activated(UPDATED_ACTIVATED)
            .deleted(UPDATED_DELETED);
        AppDTO appDTO = appMapper.toDto(updatedApp);

        restAppMockMvc.perform(put("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApp.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApp.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testApp.getImageBlob()).isEqualTo(UPDATED_IMAGE_BLOB);
        assertThat(testApp.getImageBlobContentType()).isEqualTo(UPDATED_IMAGE_BLOB_CONTENT_TYPE);
        assertThat(testApp.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testApp.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testApp.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc.perform(put("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeDelete = appRepository.findAll().size();

        // Get the app
        restAppMockMvc.perform(delete("/api/apps/{id}", app.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(App.class);
        App app1 = new App();
        app1.setId(1L);
        App app2 = new App();
        app2.setId(app1.getId());
        assertThat(app1).isEqualTo(app2);
        app2.setId(2L);
        assertThat(app1).isNotEqualTo(app2);
        app1.setId(null);
        assertThat(app1).isNotEqualTo(app2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppDTO.class);
        AppDTO appDTO1 = new AppDTO();
        appDTO1.setId(1L);
        AppDTO appDTO2 = new AppDTO();
        assertThat(appDTO1).isNotEqualTo(appDTO2);
        appDTO2.setId(appDTO1.getId());
        assertThat(appDTO1).isEqualTo(appDTO2);
        appDTO2.setId(2L);
        assertThat(appDTO1).isNotEqualTo(appDTO2);
        appDTO1.setId(null);
        assertThat(appDTO1).isNotEqualTo(appDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appMapper.fromId(null)).isNull();
    }
}
