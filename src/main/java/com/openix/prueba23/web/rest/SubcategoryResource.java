package com.openix.prueba23.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.openix.prueba23.service.SubcategoryService;
import com.openix.prueba23.web.rest.errors.BadRequestAlertException;
import com.openix.prueba23.web.rest.util.HeaderUtil;
import com.openix.prueba23.web.rest.util.PaginationUtil;
import com.openix.prueba23.service.dto.SubcategoryDTO;
import com.openix.prueba23.service.dto.SubcategoryCriteria;
import com.openix.prueba23.service.SubcategoryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Subcategory.
 */
@RestController
@RequestMapping("/api")
public class SubcategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubcategoryResource.class);

    private static final String ENTITY_NAME = "subcategory";

    private final SubcategoryService subcategoryService;

    private final SubcategoryQueryService subcategoryQueryService;

    public SubcategoryResource(SubcategoryService subcategoryService, SubcategoryQueryService subcategoryQueryService) {
        this.subcategoryService = subcategoryService;
        this.subcategoryQueryService = subcategoryQueryService;
    }

    /**
     * POST  /subcategories : Create a new subcategory.
     *
     * @param subcategoryDTO the subcategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subcategoryDTO, or with status 400 (Bad Request) if the subcategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subcategories")
    @Timed
    public ResponseEntity<SubcategoryDTO> createSubcategory(@Valid @RequestBody SubcategoryDTO subcategoryDTO) throws URISyntaxException {
        log.debug("REST request to save Subcategory : {}", subcategoryDTO);
        if (subcategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new subcategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubcategoryDTO result = subcategoryService.save(subcategoryDTO);
        return ResponseEntity.created(new URI("/api/subcategories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subcategories : Updates an existing subcategory.
     *
     * @param subcategoryDTO the subcategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subcategoryDTO,
     * or with status 400 (Bad Request) if the subcategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the subcategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subcategories")
    @Timed
    public ResponseEntity<SubcategoryDTO> updateSubcategory(@Valid @RequestBody SubcategoryDTO subcategoryDTO) throws URISyntaxException {
        log.debug("REST request to update Subcategory : {}", subcategoryDTO);
        if (subcategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubcategoryDTO result = subcategoryService.save(subcategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subcategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subcategories : get all the subcategories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of subcategories in body
     */
    @GetMapping("/subcategories")
    @Timed
    public ResponseEntity<List<SubcategoryDTO>> getAllSubcategories(SubcategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Subcategories by criteria: {}", criteria);
        Page<SubcategoryDTO> page = subcategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subcategories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /subcategories/count : count all the subcategories.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/subcategories/count")
    @Timed
    public ResponseEntity<Long> countSubcategories(SubcategoryCriteria criteria) {
        log.debug("REST request to count Subcategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(subcategoryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /subcategories/:id : get the "id" subcategory.
     *
     * @param id the id of the subcategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subcategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/subcategories/{id}")
    @Timed
    public ResponseEntity<SubcategoryDTO> getSubcategory(@PathVariable Long id) {
        log.debug("REST request to get Subcategory : {}", id);
        Optional<SubcategoryDTO> subcategoryDTO = subcategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subcategoryDTO);
    }

    /**
     * DELETE  /subcategories/:id : delete the "id" subcategory.
     *
     * @param id the id of the subcategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subcategories/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        log.debug("REST request to delete Subcategory : {}", id);
        subcategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
