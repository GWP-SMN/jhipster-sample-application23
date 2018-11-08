package com.openix.prueba23.service.mapper;

import com.openix.prueba23.domain.*;
import com.openix.prueba23.service.dto.KeyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Key and its DTO KeyDTO.
 */
@Mapper(componentModel = "spring", uses = {RegistryMapper.class, AppMapper.class})
public interface KeyMapper extends EntityMapper<KeyDTO, Key> {

    @Mapping(source = "registry.id", target = "registryId")
    @Mapping(source = "app.id", target = "appId")
    @Mapping(source = "app.name", target = "appName")
    KeyDTO toDto(Key key);

    @Mapping(source = "registryId", target = "registry")
    @Mapping(source = "appId", target = "app")
    Key toEntity(KeyDTO keyDTO);

    default Key fromId(Long id) {
        if (id == null) {
            return null;
        }
        Key key = new Key();
        key.setId(id);
        return key;
    }
}
