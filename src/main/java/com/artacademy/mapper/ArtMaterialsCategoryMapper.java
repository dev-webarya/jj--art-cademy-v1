package com.artacademy.mapper;

import com.artacademy.dto.request.ArtMaterialsCategoryRequestDto;
import com.artacademy.dto.response.ArtMaterialsCategoryResponseDto;
import com.artacademy.entity.ArtMaterialsCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtMaterialsCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtMaterialsCategory toEntity(ArtMaterialsCategoryRequestDto dto);

    @Mapping(target = "parentId", source = "parent.categoryId")
    @Mapping(target = "parentName", source = "parent.name")
    ArtMaterialsCategoryResponseDto toDto(ArtMaterialsCategory entity);

    List<ArtMaterialsCategoryResponseDto> toDtoList(List<ArtMaterialsCategory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtMaterialsCategoryRequestDto dto, @MappingTarget ArtMaterialsCategory entity);
}
