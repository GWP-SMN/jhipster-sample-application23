package com.openix.prueba23.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.openix.prueba23.service.RegistryService;
import com.openix.prueba23.web.rest.errors.BadRequestAlertException;
import com.openix.prueba23.web.rest.util.HeaderUtil;
import com.openix.prueba23.web.rest.util.PaginationUtil;
import com.openix.prueba23.service.dto.RegistryDTO;
import com.openix.prueba23.service.dto.RegistryCriteria;
import com.openix.prueba23.service.RegistryQueryService;
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
 * REST controller for managing Registry.
 */
@RestController
@RequestMapping("/api")
public class RegistryResource {

    private final Logger log = LoggerFactory.getLogger(RegistryResource.class);

    private static final String ENTITY_NAME = "registry";

    private final RegistryService registryService;

    private final RegistryQueryService registryQueryService;

    public RegistryResource(RegistryService registryService, RegistryQueryService registryQueryService) {
        this.registryService = registryService;
        this.registryQueryService = registryQueryService;
    }

    /**
     * POST  /registries : Create a new registry.
     *
     * @param registryDTO the registryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registryDTO, or with status 400 (Bad Request) if the registry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/registries")
    @Timed
    public ResponseEntity<RegistryDTO> createRegistry(@Valid @RequestBody RegistryDTO registryDTO) throws URISyntaxException {
        log.debug("REST request to save Registry : {}", registryDTO);
        if (registryDTO.getId() != null) {
            throw new BadRequestAlertException("A new registry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistryDTO result = registryService.save(registryDTO);
        return ResponseEntity.created(new URI("/api/registries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registries : Updates an existing registry.
     *
     * @param registryDTO the registryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registryDTO,
     * or with status 400 (Bad Request) if the registryDTO is not valid,
     * or with status 500 (Internal Server Error) if the registryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/registries")
    @Timed
    public ResponseEntity<RegistryDTO> updateRegistry(@Valid @RequestBody RegistryDTO registryDTO) throws URISyntaxException {
        log.debug("REST request to update Registry : {}", registryDTO);
        if (registryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegistryDTO result = registryService.save(registryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, registryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registries : get all the registries.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of registries in body
     */
    @GetMapping("/registries")
    @Timed
    public ResponseEntity<List<RegistryDTO>> getAllRegistries(RegistryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Registries by criteria: {}", criteria);
        Page<RegistryDTO> page = registryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/registries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /registries/count : count all the registries.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/registries/count")
    @Timed
    public ResponseEntity<Long> countRegistries(RegistryCriteria criteria) {
        log.debug("REST request to count Registries by criteria: {}", criteria);
        return ResponseEntity.ok().body(registryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /registries/:id : get the "id" registry.
     *
     * @param id the id of the registryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/registries/{id}")
    @Timed
    public ResponseEntity<RegistryDTO> getRegistry(@PathVariable Long id) {
        log.debug("REST request to get Registry : {}", id);
        Optional<RegistryDTO> registryDTO = registryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registryDTO);
    }

    /**
     * DELETE  /registries/:id : delete the "id" registry.
     *
     * @param id the id of the registryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/registries/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegistry(@PathVariable Long id) {
        log.debug("REST request to delete Registry : {}", id);
        registryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
