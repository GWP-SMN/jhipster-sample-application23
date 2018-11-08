package com.openix.prueba23.service.mapper;

import com.openix.prueba23.domain.*;
import com.openix.prueba23.service.dto.ValidationLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ValidationLog and its DTO ValidationLogDTO.
 */
@Mapper(componentModel = "spring", uses = {RegistryMapper.class})
public interface ValidationLogMapper extends EntityMapper<ValidationLogDTO, ValidationLog> {

    @Mapping(source = "registry.id", target = "registryId")
    ValidationLogDTO toDto(ValidationLog validationLog);

    @Mapping(source = "registryId", target = "registry")
    ValidationLog toEntity(ValidationLogDTO validationLogDTO);

    default ValidationLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        ValidationLog validationLog = new ValidationLog();
        validationLog.setId(id);
        return validationLog;
    }
}
