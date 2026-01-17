package com.artacademy.mapper;

import com.artacademy.dto.request.ArtClassesCategoryRequestDto;
import com.artacademy.dto.response.ArtClassesCategoryResponseDto;
import com.artacademy.entity.ArtClassesCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtClassesCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtClassesCategory toEntity(ArtClassesCategoryRequestDto dto);

    @Mapping(target = "parentId", source = "parent.categoryId")
    @Mapping(target = "parentName", source = "parent.name")
    ArtClassesCategoryResponseDto toDto(ArtClassesCategory entity);

    List<ArtClassesCategoryResponseDto> toDtoList(List<ArtClassesCategory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtClassesCategoryRequestDto dto, @MappingTarget ArtClassesCategory entity);
}
