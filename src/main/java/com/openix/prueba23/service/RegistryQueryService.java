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

import com.openix.prueba23.domain.Registry;
import com.openix.prueba23.domain.*; // for static metamodels
import com.openix.prueba23.repository.RegistryRepository;
import com.openix.prueba23.service.dto.RegistryCriteria;
import com.openix.prueba23.service.dto.RegistryDTO;
import com.openix.prueba23.service.mapper.RegistryMapper;

/**
 * Service for executing complex queries for Registry entities in the database.
 * The main input is a {@link RegistryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegistryDTO} or a {@link Page} of {@link RegistryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegistryQueryService extends QueryService<Registry> {

    private final Logger log = LoggerFactory.getLogger(RegistryQueryService.class);

    private final RegistryRepository registryRepository;

    private final RegistryMapper registryMapper;

    public RegistryQueryService(RegistryRepository registryRepository, RegistryMapper registryMapper) {
        this.registryRepository = registryRepository;
        this.registryMapper = registryMapper;
    }

    /**
     * Return a {@link List} of {@link RegistryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegistryDTO> findByCriteria(RegistryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Registry> specification = createSpecification(criteria);
        return registryMapper.toDto(registryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RegistryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegistryDTO> findByCriteria(RegistryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Registry> specification = createSpecification(criteria);
        return registryRepository.findAll(specification, page)
            .map(registryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegistryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Registry> specification = createSpecification(criteria);
        return registryRepository.count(specification);
    }

    /**
     * Function to convert RegistryCriteria to a {@link Specification}
     */
    private Specification<Registry> createSpecification(RegistryCriteria criteria) {
        Specification<Registry> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Registry_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Registry_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Registry_.lastName));
            }
            if (criteria.getDni() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDni(), Registry_.dni));
            }
            if (criteria.getBirthdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthdate(), Registry_.birthdate));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Registry_.phoneNumber));
            }
            if (criteria.getBusiness() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusiness(), Registry_.business));
            }
            if (criteria.getOccupation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOccupation(), Registry_.occupation));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Registry_.email));
            }
            if (criteria.getActivationKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActivationKey(), Registry_.activationKey));
            }
            if (criteria.getValidationCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValidationCode(), Registry_.validationCode));
            }
            if (criteria.getResetKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResetKey(), Registry_.resetKey));
            }
            if (criteria.getResetDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResetDate(), Registry_.resetDate));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Registry_.activated));
            }
            if (criteria.getActivationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivationDate(), Registry_.activationDate));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), Registry_.deleted));
            }
            if (criteria.getAppId() != null) {
                specification = specification.and(buildSpecification(criteria.getAppId(),
                    root -> root.join(Registry_.app, JoinType.LEFT).get(App_.id)));
            }
            if (criteria.getSubcategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getSubcategoryId(),
                    root -> root.join(Registry_.subcategory, JoinType.LEFT).get(Subcategory_.id)));
            }
        }
        return specification;
    }
}
