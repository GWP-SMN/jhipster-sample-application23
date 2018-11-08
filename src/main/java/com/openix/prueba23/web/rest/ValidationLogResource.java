package com.openix.prueba23.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.openix.prueba23.service.ValidationLogService;
import com.openix.prueba23.web.rest.errors.BadRequestAlertException;
import com.openix.prueba23.web.rest.util.HeaderUtil;
import com.openix.prueba23.web.rest.util.PaginationUtil;
import com.openix.prueba23.service.dto.ValidationLogDTO;
import com.openix.prueba23.service.dto.ValidationLogCriteria;
import com.openix.prueba23.service.ValidationLogQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ValidationLog.
 */
@RestController
@RequestMapping("/api")
public class ValidationLogResource {

    private final Logger log = LoggerFactory.getLogger(ValidationLogResource.class);

    private static final String ENTITY_NAME = "validationLog";

    private final ValidationLogService validationLogService;

    private final ValidationLogQueryService validationLogQueryService;

    public ValidationLogResource(ValidationLogService validationLogService, ValidationLogQueryService validationLogQueryService) {
        this.validationLogService = validationLogService;
        this.validationLogQueryService = validationLogQueryService;
    }

    /**
     * POST  /validation-logs : Create a new validationLog.
     *
     * @param validationLogDTO the validationLogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new validationLogDTO, or with status 400 (Bad Request) if the validationLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/validation-logs")
    @Timed
    public ResponseEntity<ValidationLogDTO> createValidationLog(@RequestBody ValidationLogDTO validationLogDTO) throws URISyntaxException {
        log.debug("REST request to save ValidationLog : {}", validationLogDTO);
        if (validationLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new validationLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValidationLogDTO result = validationLogService.save(validationLogDTO);
        return ResponseEntity.created(new URI("/api/validation-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /validation-logs : Updates an existing validationLog.
     *
     * @param validationLogDTO the validationLogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated validationLogDTO,
     * or with status 400 (Bad Request) if the validationLogDTO is not valid,
     * or with status 500 (Internal Server Error) if the validationLogDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/validation-logs")
    @Timed
    public ResponseEntity<ValidationLogDTO> updateValidationLog(@RequestBody ValidationLogDTO validationLogDTO) throws URISyntaxException {
        log.debug("REST request to update ValidationLog : {}", validationLogDTO);
        if (validationLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValidationLogDTO result = validationLogService.save(validationLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, validationLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /validation-logs : get all the validationLogs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of validationLogs in body
     */
    @GetMapping("/validation-logs")
    @Timed
    public ResponseEntity<List<ValidationLogDTO>> getAllValidationLogs(ValidationLogCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ValidationLogs by criteria: {}", criteria);
        Page<ValidationLogDTO> page = validationLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/validation-logs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /validation-logs/count : count all the validationLogs.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/validation-logs/count")
    @Timed
    public ResponseEntity<Long> countValidationLogs(ValidationLogCriteria criteria) {
        log.debug("REST request to count ValidationLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(validationLogQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /validation-logs/:id : get the "id" validationLog.
     *
     * @param id the id of the validationLogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the validationLogDTO, or with status 404 (Not Found)
     */
    @GetMapping("/validation-logs/{id}")
    @Timed
    public ResponseEntity<ValidationLogDTO> getValidationLog(@PathVariable Long id) {
        log.debug("REST request to get ValidationLog : {}", id);
        Optional<ValidationLogDTO> validationLogDTO = validationLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(validationLogDTO);
    }

    /**
     * DELETE  /validation-logs/:id : delete the "id" validationLog.
     *
     * @param id the id of the validationLogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/validation-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteValidationLog(@PathVariable Long id) {
        log.debug("REST request to delete ValidationLog : {}", id);
        validationLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
