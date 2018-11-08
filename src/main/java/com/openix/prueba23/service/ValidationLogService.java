package com.openix.prueba23.service;

import com.openix.prueba23.domain.ValidationLog;
import com.openix.prueba23.repository.ValidationLogRepository;
import com.openix.prueba23.service.dto.ValidationLogDTO;
import com.openix.prueba23.service.mapper.ValidationLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing ValidationLog.
 */
@Service
@Transactional
public class ValidationLogService {

    private final Logger log = LoggerFactory.getLogger(ValidationLogService.class);

    private final ValidationLogRepository validationLogRepository;

    private final ValidationLogMapper validationLogMapper;

    public ValidationLogService(ValidationLogRepository validationLogRepository, ValidationLogMapper validationLogMapper) {
        this.validationLogRepository = validationLogRepository;
        this.validationLogMapper = validationLogMapper;
    }

    /**
     * Save a validationLog.
     *
     * @param validationLogDTO the entity to save
     * @return the persisted entity
     */
    public ValidationLogDTO save(ValidationLogDTO validationLogDTO) {
        log.debug("Request to save ValidationLog : {}", validationLogDTO);

        ValidationLog validationLog = validationLogMapper.toEntity(validationLogDTO);
        validationLog = validationLogRepository.save(validationLog);
        return validationLogMapper.toDto(validationLog);
    }

    /**
     * Get all the validationLogs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ValidationLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ValidationLogs");
        return validationLogRepository.findAll(pageable)
            .map(validationLogMapper::toDto);
    }


    /**
     * Get one validationLog by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ValidationLogDTO> findOne(Long id) {
        log.debug("Request to get ValidationLog : {}", id);
        return validationLogRepository.findById(id)
            .map(validationLogMapper::toDto);
    }

    /**
     * Delete the validationLog by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ValidationLog : {}", id);
        validationLogRepository.deleteById(id);
    }
}
