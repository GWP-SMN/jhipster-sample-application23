package com.openix.prueba23.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.openix.prueba23.service.AppService;
import com.openix.prueba23.web.rest.errors.BadRequestAlertException;
import com.openix.prueba23.web.rest.util.HeaderUtil;
import com.openix.prueba23.web.rest.util.PaginationUtil;
import com.openix.prueba23.service.dto.AppDTO;
import com.openix.prueba23.service.dto.AppCriteria;
import com.openix.prueba23.service.AppQueryService;
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
 * REST controller for managing App.
 */
@RestController
@RequestMapping("/api")
public class AppResource {

    private final Logger log = LoggerFactory.getLogger(AppResource.class);

    private static final String ENTITY_NAME = "app";

    private final AppService appService;

    private final AppQueryService appQueryService;

    public AppResource(AppService appService, AppQueryService appQueryService) {
        this.appService = appService;
        this.appQueryService = appQueryService;
    }

    /**
     * POST  /apps : Create a new app.
     *
     * @param appDTO the appDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appDTO, or with status 400 (Bad Request) if the app has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apps")
    @Timed
    public ResponseEntity<AppDTO> createApp(@Valid @RequestBody AppDTO appDTO) throws URISyntaxException {
        log.debug("REST request to save App : {}", appDTO);
        if (appDTO.getId() != null) {
            throw new BadRequestAlertException("A new app cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppDTO result = appService.save(appDTO);
        return ResponseEntity.created(new URI("/api/apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apps : Updates an existing app.
     *
     * @param appDTO the appDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appDTO,
     * or with status 400 (Bad Request) if the appDTO is not valid,
     * or with status 500 (Internal Server Error) if the appDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apps")
    @Timed
    public ResponseEntity<AppDTO> updateApp(@Valid @RequestBody AppDTO appDTO) throws URISyntaxException {
        log.debug("REST request to update App : {}", appDTO);
        if (appDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppDTO result = appService.save(appDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apps : get all the apps.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of apps in body
     */
    @GetMapping("/apps")
    @Timed
    public ResponseEntity<List<AppDTO>> getAllApps(AppCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Apps by criteria: {}", criteria);
        Page<AppDTO> page = appQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apps");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /apps/count : count all the apps.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/apps/count")
    @Timed
    public ResponseEntity<Long> countApps(AppCriteria criteria) {
        log.debug("REST request to count Apps by criteria: {}", criteria);
        return ResponseEntity.ok().body(appQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /apps/:id : get the "id" app.
     *
     * @param id the id of the appDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appDTO, or with status 404 (Not Found)
     */
    @GetMapping("/apps/{id}")
    @Timed
    public ResponseEntity<AppDTO> getApp(@PathVariable Long id) {
        log.debug("REST request to get App : {}", id);
        Optional<AppDTO> appDTO = appService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appDTO);
    }

    /**
     * DELETE  /apps/:id : delete the "id" app.
     *
     * @param id the id of the appDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apps/{id}")
    @Timed
    public ResponseEntity<Void> deleteApp(@PathVariable Long id) {
        log.debug("REST request to delete App : {}", id);
        appService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
