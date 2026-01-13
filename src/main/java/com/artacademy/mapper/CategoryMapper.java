package com.artacademy.mapper;

import com.artacademy.dto.request.CategoryRequestDto;
import com.artacademy.dto.response.CategoryResponseDto;
import com.artacademy.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    // Maps Entity to Response DTO
    // It automatically maps category.parent.id to categoryResponseDto.parentId
    @Mapping(target = "parentId", source = "parent.id")
    CategoryResponseDto toDto(Category category);

    // Maps Request DTO to Entity
    // We ignore parentId because it will be handled by the service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequestDto categoryRequestDto);

    // Updates an existing entity from a Request DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CategoryRequestDto categoryRequestDto, @MappingTarget Category category);
}