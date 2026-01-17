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

    @Mapping(target = "categoryName", ignore = true) // Would need separate category lookup
    ArtMaterialsResponseDto toDto(ArtMaterials entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtMaterialsRequestDto dto, @MappingTarget ArtMaterials entity);
}
