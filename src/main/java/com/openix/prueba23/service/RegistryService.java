package com.openix.prueba23.service;

import com.openix.prueba23.domain.Registry;
import com.openix.prueba23.repository.RegistryRepository;
import com.openix.prueba23.service.dto.RegistryDTO;
import com.openix.prueba23.service.mapper.RegistryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Registry.
 */
@Service
@Transactional
public class RegistryService {

    private final Logger log = LoggerFactory.getLogger(RegistryService.class);

    private final RegistryRepository registryRepository;

    private final RegistryMapper registryMapper;

    public RegistryService(RegistryRepository registryRepository, RegistryMapper registryMapper) {
        this.registryRepository = registryRepository;
        this.registryMapper = registryMapper;
    }

    /**
     * Save a registry.
     *
     * @param registryDTO the entity to save
     * @return the persisted entity
     */
    public RegistryDTO save(RegistryDTO registryDTO) {
        log.debug("Request to save Registry : {}", registryDTO);

        Registry registry = registryMapper.toEntity(registryDTO);
        registry = registryRepository.save(registry);
        return registryMapper.toDto(registry);
    }

    /**
     * Get all the registries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RegistryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Registries");
        return registryRepository.findAll(pageable)
            .map(registryMapper::toDto);
    }


    /**
     * Get one registry by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RegistryDTO> findOne(Long id) {
        log.debug("Request to get Registry : {}", id);
        return registryRepository.findById(id)
            .map(registryMapper::toDto);
    }

    /**
     * Delete the registry by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Registry : {}", id);
        registryRepository.deleteById(id);
    }
}
