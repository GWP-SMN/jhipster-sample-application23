package com.openix.prueba23.web.rest;

import com.openix.prueba23.JhipsterSampleApplicationWithGradleApp;

import com.openix.prueba23.domain.Subcategory;
import com.openix.prueba23.domain.Category;
import com.openix.prueba23.repository.SubcategoryRepository;
import com.openix.prueba23.service.SubcategoryService;
import com.openix.prueba23.service.dto.SubcategoryDTO;
import com.openix.prueba23.service.mapper.SubcategoryMapper;
import com.openix.prueba23.web.rest.errors.ExceptionTranslator;
import com.openix.prueba23.service.dto.SubcategoryCriteria;
import com.openix.prueba23.service.SubcategoryQueryService;

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
 * Test class for the SubcategoryResource REST controller.
 *
 * @see SubcategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationWithGradleApp.class)
public class SubcategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private SubcategoryMapper subcategoryMapper;

    @Autowired
    private SubcategoryService subcategoryService;

    @Autowired
    private SubcategoryQueryService subcategoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubcategoryMockMvc;

    private Subcategory subcategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubcategoryResource subcategoryResource = new SubcategoryResource(subcategoryService, subcategoryQueryService);
        this.restSubcategoryMockMvc = MockMvcBuilders.standaloneSetup(subcategoryResource)
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
    public static Subcategory createEntity(EntityManager em) {
        Subcategory subcategory = new Subcategory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .deleted(DEFAULT_DELETED);
        return subcategory;
    }

    @Before
    public void initTest() {
        subcategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubcategory() throws Exception {
        int databaseSizeBeforeCreate = subcategoryRepository.findAll().size();

        // Create the Subcategory
        SubcategoryDTO subcategoryDTO = subcategoryMapper.toDto(subcategory);
        restSubcategoryMockMvc.perform(post("/api/subcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subcategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Subcategory in the database
        List<Subcategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeCreate + 1);
        Subcategory testSubcategory = subcategoryList.get(subcategoryList.size() - 1);
        assertThat(testSubcategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubcategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubcategory.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createSubcategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subcategoryRepository.findAll().size();

        // Create the Subcategory with an existing ID
        subcategory.setId(1L);
        SubcategoryDTO subcategoryDTO = subcategoryMapper.toDto(subcategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubcategoryMockMvc.perform(post("/api/subcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subcategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subcategory in the database
        List<Subcategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subcategoryRepository.findAll().size();
        // set the field null
        subcategory.setName(null);

        // Create the Subcategory, which fails.
        SubcategoryDTO subcategoryDTO = subcategoryMapper.toDto(subcategory);

        restSubcategoryMockMvc.perform(post("/api/subcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subcategoryDTO)))
            .andExpect(status().isBadRequest());

        List<Subcategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubcategories() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList
        restSubcategoryMockMvc.perform(get("/api/subcategories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subcategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSubcategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get the subcategory
        restSubcategoryMockMvc.perform(get("/api/subcategories/{id}", subcategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subcategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where name equals to DEFAULT_NAME
        defaultSubcategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the subcategoryList where name equals to UPDATED_NAME
        defaultSubcategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSubcategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the subcategoryList where name equals to UPDATED_NAME
        defaultSubcategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where name is not null
        defaultSubcategoryShouldBeFound("name.specified=true");

        // Get all the subcategoryList where name is null
        defaultSubcategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where description equals to DEFAULT_DESCRIPTION
        defaultSubcategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the subcategoryList where description equals to UPDATED_DESCRIPTION
        defaultSubcategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSubcategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the subcategoryList where description equals to UPDATED_DESCRIPTION
        defaultSubcategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where description is not null
        defaultSubcategoryShouldBeFound("description.specified=true");

        // Get all the subcategoryList where description is null
        defaultSubcategoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where deleted equals to DEFAULT_DELETED
        defaultSubcategoryShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the subcategoryList where deleted equals to UPDATED_DELETED
        defaultSubcategoryShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultSubcategoryShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the subcategoryList where deleted equals to UPDATED_DELETED
        defaultSubcategoryShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList where deleted is not null
        defaultSubcategoryShouldBeFound("deleted.specified=true");

        // Get all the subcategoryList where deleted is null
        defaultSubcategoryShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubcategoriesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        subcategory.setCategory(category);
        subcategoryRepository.saveAndFlush(subcategory);
        Long categoryId = category.getId();

        // Get all the subcategoryList where category equals to categoryId
        defaultSubcategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the subcategoryList where category equals to categoryId + 1
        defaultSubcategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSubcategoryShouldBeFound(String filter) throws Exception {
        restSubcategoryMockMvc.perform(get("/api/subcategories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subcategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restSubcategoryMockMvc.perform(get("/api/subcategories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSubcategoryShouldNotBeFound(String filter) throws Exception {
        restSubcategoryMockMvc.perform(get("/api/subcategories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubcategoryMockMvc.perform(get("/api/subcategories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSubcategory() throws Exception {
        // Get the subcategory
        restSubcategoryMockMvc.perform(get("/api/subcategories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubcategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        int databaseSizeBeforeUpdate = subcategoryRepository.findAll().size();

        // Update the subcategory
        Subcategory updatedSubcategory = subcategoryRepository.findById(subcategory.getId()).get();
        // Disconnect from session so that the updates on updatedSubcategory are not directly saved in db
        em.detach(updatedSubcategory);
        updatedSubcategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED);
        SubcategoryDTO subcategoryDTO = subcategoryMapper.toDto(updatedSubcategory);

        restSubcategoryMockMvc.perform(put("/api/subcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subcategoryDTO)))
            .andExpect(status().isOk());

        // Validate the Subcategory in the database
        List<Subcategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeUpdate);
        Subcategory testSubcategory = subcategoryList.get(subcategoryList.size() - 1);
        assertThat(testSubcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubcategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubcategory.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = subcategoryRepository.findAll().size();

        // Create the Subcategory
        SubcategoryDTO subcategoryDTO = subcategoryMapper.toDto(subcategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubcategoryMockMvc.perform(put("/api/subcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subcategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subcategory in the database
        List<Subcategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubcategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        int databaseSizeBeforeDelete = subcategoryRepository.findAll().size();

        // Get the subcategory
        restSubcategoryMockMvc.perform(delete("/api/subcategories/{id}", subcategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Subcategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subcategory.class);
        Subcategory subcategory1 = new Subcategory();
        subcategory1.setId(1L);
        Subcategory subcategory2 = new Subcategory();
        subcategory2.setId(subcategory1.getId());
        assertThat(subcategory1).isEqualTo(subcategory2);
        subcategory2.setId(2L);
        assertThat(subcategory1).isNotEqualTo(subcategory2);
        subcategory1.setId(null);
        assertThat(subcategory1).isNotEqualTo(subcategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubcategoryDTO.class);
        SubcategoryDTO subcategoryDTO1 = new SubcategoryDTO();
        subcategoryDTO1.setId(1L);
        SubcategoryDTO subcategoryDTO2 = new SubcategoryDTO();
        assertThat(subcategoryDTO1).isNotEqualTo(subcategoryDTO2);
        subcategoryDTO2.setId(subcategoryDTO1.getId());
        assertThat(subcategoryDTO1).isEqualTo(subcategoryDTO2);
        subcategoryDTO2.setId(2L);
        assertThat(subcategoryDTO1).isNotEqualTo(subcategoryDTO2);
        subcategoryDTO1.setId(null);
        assertThat(subcategoryDTO1).isNotEqualTo(subcategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subcategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subcategoryMapper.fromId(null)).isNull();
    }
}
