package com.artacademy.mapper;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.entity.ArtMaterials;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtMaterialsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtMaterials toEntity(ArtMaterialsRequestDto dto);

    // categoryName is populated in the entity by the service, so we map it directly
    ArtMaterialsResponseDto toDto(ArtMaterials entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtMaterialsRequestDto dto, @MappingTarget ArtMaterials entity);

    // Variant mappings
    ArtMaterials.MaterialVariant toVariantEntity(com.artacademy.dto.request.MaterialVariantRequestDto dto);

    com.artacademy.dto.response.MaterialVariantResponseDto toVariantDto(ArtMaterials.MaterialVariant entity);
}
