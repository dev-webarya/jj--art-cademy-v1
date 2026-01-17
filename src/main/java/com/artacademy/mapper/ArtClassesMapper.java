package com.artacademy.mapper;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.entity.ArtClasses;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtClassesMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    ArtClasses toEntity(ArtClassesRequestDto dto);

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.name")
    ArtClassesResponseDto toDto(ArtClasses entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    void updateEntity(ArtClassesRequestDto dto, @MappingTarget ArtClasses entity);
}
