package com.artacademy.mapper;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.entity.ArtMaterials;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtMaterialsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtMaterials toEntity(ArtMaterialsRequestDto dto);

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.name")
    ArtMaterialsResponseDto toDto(ArtMaterials entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtMaterialsRequestDto dto, @MappingTarget ArtMaterials entity);
}
