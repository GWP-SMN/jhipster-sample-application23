package com.openix.prueba23.service;

import com.openix.prueba23.domain.Key;
import com.openix.prueba23.repository.KeyRepository;
import com.openix.prueba23.service.dto.KeyDTO;
import com.openix.prueba23.service.mapper.KeyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Key.
 */
@Service
@Transactional
public class KeyService {

    private final Logger log = LoggerFactory.getLogger(KeyService.class);

    private final KeyRepository keyRepository;

    private final KeyMapper keyMapper;

    public KeyService(KeyRepository keyRepository, KeyMapper keyMapper) {
        this.keyRepository = keyRepository;
        this.keyMapper = keyMapper;
    }

    /**
     * Save a key.
     *
     * @param keyDTO the entity to save
     * @return the persisted entity
     */
    public KeyDTO save(KeyDTO keyDTO) {
        log.debug("Request to save Key : {}", keyDTO);

        Key key = keyMapper.toEntity(keyDTO);
        key = keyRepository.save(key);
        return keyMapper.toDto(key);
    }

    /**
     * Get all the keys.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<KeyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Keys");
        return keyRepository.findAll(pageable)
            .map(keyMapper::toDto);
    }


    /**
     * Get one key by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<KeyDTO> findOne(Long id) {
        log.debug("Request to get Key : {}", id);
        return keyRepository.findById(id)
            .map(keyMapper::toDto);
    }

    /**
     * Delete the key by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Key : {}", id);
        keyRepository.deleteById(id);
    }
}
