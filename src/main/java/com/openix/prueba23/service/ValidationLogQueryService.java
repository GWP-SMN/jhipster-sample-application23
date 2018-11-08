package com.openix.prueba23.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.openix.prueba23.domain.ValidationLog;
import com.openix.prueba23.domain.*; // for static metamodels
import com.openix.prueba23.repository.ValidationLogRepository;
import com.openix.prueba23.service.dto.ValidationLogCriteria;
import com.openix.prueba23.service.dto.ValidationLogDTO;
import com.openix.prueba23.service.mapper.ValidationLogMapper;

/**
 * Service for executing complex queries for ValidationLog entities in the database.
 * The main input is a {@link ValidationLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ValidationLogDTO} or a {@link Page} of {@link ValidationLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ValidationLogQueryService extends QueryService<ValidationLog> {

    private final Logger log = LoggerFactory.getLogger(ValidationLogQueryService.class);

    private final ValidationLogRepository validationLogRepository;

    private final ValidationLogMapper validationLogMapper;

    public ValidationLogQueryService(ValidationLogRepository validationLogRepository, ValidationLogMapper validationLogMapper) {
        this.validationLogRepository = validationLogRepository;
        this.validationLogMapper = validationLogMapper;
    }

    /**
     * Return a {@link List} of {@link ValidationLogDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ValidationLogDTO> findByCriteria(ValidationLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ValidationLog> specification = createSpecification(criteria);
        return validationLogMapper.toDto(validationLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ValidationLogDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ValidationLogDTO> findByCriteria(ValidationLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ValidationLog> specification = createSpecification(criteria);
        return validationLogRepository.findAll(specification, page)
            .map(validationLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ValidationLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ValidationLog> specification = createSpecification(criteria);
        return validationLogRepository.count(specification);
    }

    /**
     * Function to convert ValidationLogCriteria to a {@link Specification}
     */
    private Specification<ValidationLog> createSpecification(ValidationLogCriteria criteria) {
        Specification<ValidationLog> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ValidationLog_.id));
            }
            if (criteria.getDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTime(), ValidationLog_.dateTime));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), ValidationLog_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), ValidationLog_.longitude));
            }
            if (criteria.getSuccess() != null) {
                specification = specification.and(buildSpecification(criteria.getSuccess(), ValidationLog_.success));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), ValidationLog_.deleted));
            }
            if (criteria.getRegistryId() != null) {
                specification = specification.and(buildSpecification(criteria.getRegistryId(),
                    root -> root.join(ValidationLog_.registry, JoinType.LEFT).get(Registry_.id)));
            }
        }
        return specification;
    }
}
