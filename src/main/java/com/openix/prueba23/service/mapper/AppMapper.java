package com.openix.prueba23.service.mapper;

import com.openix.prueba23.domain.*;
import com.openix.prueba23.service.dto.AppDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity App and its DTO AppDTO.
 */
@Mapper(componentModel = "spring", uses = {SubcategoryMapper.class})
public interface AppMapper extends EntityMapper<AppDTO, App> {

    @Mapping(source = "subcategory.id", target = "subcategoryId")
    @Mapping(source = "subcategory.name", target = "subcategoryName")
    AppDTO toDto(App app);

    @Mapping(source = "subcategoryId", target = "subcategory")
    App toEntity(AppDTO appDTO);

    default App fromId(Long id) {
        if (id == null) {
            return null;
        }
        App app = new App();
        app.setId(id);
        return app;
    }
}
