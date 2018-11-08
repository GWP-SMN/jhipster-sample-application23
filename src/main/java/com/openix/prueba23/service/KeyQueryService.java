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

import com.openix.prueba23.domain.Key;
import com.openix.prueba23.domain.*; // for static metamodels
import com.openix.prueba23.repository.KeyRepository;
import com.openix.prueba23.service.dto.KeyCriteria;
import com.openix.prueba23.service.dto.KeyDTO;
import com.openix.prueba23.service.mapper.KeyMapper;

/**
 * Service for executing complex queries for Key entities in the database.
 * The main input is a {@link KeyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KeyDTO} or a {@link Page} of {@link KeyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KeyQueryService extends QueryService<Key> {

    private final Logger log = LoggerFactory.getLogger(KeyQueryService.class);

    private final KeyRepository keyRepository;

    private final KeyMapper keyMapper;

    public KeyQueryService(KeyRepository keyRepository, KeyMapper keyMapper) {
        this.keyRepository = keyRepository;
        this.keyMapper = keyMapper;
    }

    /**
     * Return a {@link List} of {@link KeyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KeyDTO> findByCriteria(KeyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Key> specification = createSpecification(criteria);
        return keyMapper.toDto(keyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KeyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KeyDTO> findByCriteria(KeyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Key> specification = createSpecification(criteria);
        return keyRepository.findAll(specification, page)
            .map(keyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KeyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Key> specification = createSpecification(criteria);
        return keyRepository.count(specification);
    }

    /**
     * Function to convert KeyCriteria to a {@link Specification}
     */
    private Specification<Key> createSpecification(KeyCriteria criteria) {
        Specification<Key> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Key_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Key_.code));
            }
            if (criteria.getUsed() != null) {
                specification = specification.and(buildSpecification(criteria.getUsed(), Key_.used));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), Key_.deleted));
            }
            if (criteria.getRegistryId() != null) {
                specification = specification.and(buildSpecification(criteria.getRegistryId(),
                    root -> root.join(Key_.registry, JoinType.LEFT).get(Registry_.id)));
            }
            if (criteria.getAppId() != null) {
                specification = specification.and(buildSpecification(criteria.getAppId(),
                    root -> root.join(Key_.app, JoinType.LEFT).get(App_.id)));
            }
        }
        return specification;
    }
}
