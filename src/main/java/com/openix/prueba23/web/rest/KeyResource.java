package com.openix.prueba23.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.openix.prueba23.service.KeyService;
import com.openix.prueba23.web.rest.errors.BadRequestAlertException;
import com.openix.prueba23.web.rest.util.HeaderUtil;
import com.openix.prueba23.web.rest.util.PaginationUtil;
import com.openix.prueba23.service.dto.KeyDTO;
import com.openix.prueba23.service.dto.KeyCriteria;
import com.openix.prueba23.service.KeyQueryService;
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
 * REST controller for managing Key.
 */
@RestController
@RequestMapping("/api")
public class KeyResource {

    private final Logger log = LoggerFactory.getLogger(KeyResource.class);

    private static final String ENTITY_NAME = "key";

    private final KeyService keyService;

    private final KeyQueryService keyQueryService;

    public KeyResource(KeyService keyService, KeyQueryService keyQueryService) {
        this.keyService = keyService;
        this.keyQueryService = keyQueryService;
    }

    /**
     * POST  /keys : Create a new key.
     *
     * @param keyDTO the keyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new keyDTO, or with status 400 (Bad Request) if the key has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/keys")
    @Timed
    public ResponseEntity<KeyDTO> createKey(@Valid @RequestBody KeyDTO keyDTO) throws URISyntaxException {
        log.debug("REST request to save Key : {}", keyDTO);
        if (keyDTO.getId() != null) {
            throw new BadRequestAlertException("A new key cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KeyDTO result = keyService.save(keyDTO);
        return ResponseEntity.created(new URI("/api/keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /keys : Updates an existing key.
     *
     * @param keyDTO the keyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated keyDTO,
     * or with status 400 (Bad Request) if the keyDTO is not valid,
     * or with status 500 (Internal Server Error) if the keyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/keys")
    @Timed
    public ResponseEntity<KeyDTO> updateKey(@Valid @RequestBody KeyDTO keyDTO) throws URISyntaxException {
        log.debug("REST request to update Key : {}", keyDTO);
        if (keyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KeyDTO result = keyService.save(keyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, keyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /keys : get all the keys.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of keys in body
     */
    @GetMapping("/keys")
    @Timed
    public ResponseEntity<List<KeyDTO>> getAllKeys(KeyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Keys by criteria: {}", criteria);
        Page<KeyDTO> page = keyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/keys");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /keys/count : count all the keys.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/keys/count")
    @Timed
    public ResponseEntity<Long> countKeys(KeyCriteria criteria) {
        log.debug("REST request to count Keys by criteria: {}", criteria);
        return ResponseEntity.ok().body(keyQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /keys/:id : get the "id" key.
     *
     * @param id the id of the keyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the keyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/keys/{id}")
    @Timed
    public ResponseEntity<KeyDTO> getKey(@PathVariable Long id) {
        log.debug("REST request to get Key : {}", id);
        Optional<KeyDTO> keyDTO = keyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(keyDTO);
    }

    /**
     * DELETE  /keys/:id : delete the "id" key.
     *
     * @param id the id of the keyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/keys/{id}")
    @Timed
    public ResponseEntity<Void> deleteKey(@PathVariable Long id) {
        log.debug("REST request to delete Key : {}", id);
        keyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
