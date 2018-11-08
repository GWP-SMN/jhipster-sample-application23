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

import com.openix.prueba23.domain.App;
import com.openix.prueba23.domain.*; // for static metamodels
import com.openix.prueba23.repository.AppRepository;
import com.openix.prueba23.service.dto.AppCriteria;
import com.openix.prueba23.service.dto.AppDTO;
import com.openix.prueba23.service.mapper.AppMapper;

/**
 * Service for executing complex queries for App entities in the database.
 * The main input is a {@link AppCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppDTO} or a {@link Page} of {@link AppDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppQueryService extends QueryService<App> {

    private final Logger log = LoggerFactory.getLogger(AppQueryService.class);

    private final AppRepository appRepository;

    private final AppMapper appMapper;

    public AppQueryService(AppRepository appRepository, AppMapper appMapper) {
        this.appRepository = appRepository;
        this.appMapper = appMapper;
    }

    /**
     * Return a {@link List} of {@link AppDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppDTO> findByCriteria(AppCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<App> specification = createSpecification(criteria);
        return appMapper.toDto(appRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppDTO> findByCriteria(AppCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<App> specification = createSpecification(criteria);
        return appRepository.findAll(specification, page)
            .map(appMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<App> specification = createSpecification(criteria);
        return appRepository.count(specification);
    }

    /**
     * Function to convert AppCriteria to a {@link Specification}
     */
    private Specification<App> createSpecification(AppCriteria criteria) {
        Specification<App> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), App_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), App_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), App_.description));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), App_.version));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), App_.image));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), App_.activated));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), App_.deleted));
            }
            if (criteria.getSubcategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getSubcategoryId(),
                    root -> root.join(App_.subcategory, JoinType.LEFT).get(Subcategory_.id)));
            }
        }
        return specification;
    }
}
