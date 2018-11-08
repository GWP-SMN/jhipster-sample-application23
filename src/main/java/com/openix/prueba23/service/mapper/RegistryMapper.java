package com.openix.prueba23.service.mapper;

import com.openix.prueba23.domain.*;
import com.openix.prueba23.service.dto.RegistryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Registry and its DTO RegistryDTO.
 */
@Mapper(componentModel = "spring", uses = {AppMapper.class, SubcategoryMapper.class})
public interface RegistryMapper extends EntityMapper<RegistryDTO, Registry> {

    @Mapping(source = "app.id", target = "appId")
    @Mapping(source = "app.name", target = "appName")
    @Mapping(source = "subcategory.id", target = "subcategoryId")
    @Mapping(source = "subcategory.name", target = "subcategoryName")
    RegistryDTO toDto(Registry registry);

    @Mapping(source = "appId", target = "app")
    @Mapping(source = "subcategoryId", target = "subcategory")
    Registry toEntity(RegistryDTO registryDTO);

    default Registry fromId(Long id) {
        if (id == null) {
            return null;
        }
        Registry registry = new Registry();
        registry.setId(id);
        return registry;
    }
}
