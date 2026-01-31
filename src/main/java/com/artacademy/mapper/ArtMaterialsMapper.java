package com.artacademy.mapper;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.request.MaterialVariantRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.dto.response.MaterialVariantResponseDto;
import com.artacademy.entity.ArtMaterials;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtMaterialsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true) // Handled manually in Service
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtMaterials toEntity(ArtMaterialsRequestDto dto);

    ArtMaterialsResponseDto toDto(ArtMaterials entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true) // Handled manually in Service
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtMaterialsRequestDto dto, @MappingTarget ArtMaterials entity);

    // --- Variant Mappings ---
    ArtMaterials.MaterialVariant toVariantEntity(MaterialVariantRequestDto dto);
    MaterialVariantResponseDto toVariantDto(ArtMaterials.MaterialVariant entity);
}
