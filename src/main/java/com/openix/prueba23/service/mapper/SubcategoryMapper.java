package com.openix.prueba23.service.mapper;

import com.openix.prueba23.domain.*;
import com.openix.prueba23.service.dto.SubcategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Subcategory and its DTO SubcategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface SubcategoryMapper extends EntityMapper<SubcategoryDTO, Subcategory> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    SubcategoryDTO toDto(Subcategory subcategory);

    @Mapping(source = "categoryId", target = "category")
    Subcategory toEntity(SubcategoryDTO subcategoryDTO);

    default Subcategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subcategory subcategory = new Subcategory();
        subcategory.setId(id);
        return subcategory;
    }
}
