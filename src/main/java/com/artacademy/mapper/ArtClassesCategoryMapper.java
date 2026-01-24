package com.artacademy.mapper;

import com.artacademy.dto.request.ArtClassesCategoryRequestDto;
import com.artacademy.dto.response.ArtClassesCategoryResponseDto;
import com.artacademy.entity.ArtClassesCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtClassesCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtClassesCategory toEntity(ArtClassesCategoryRequestDto dto);

    ArtClassesCategoryResponseDto toDto(ArtClassesCategory entity);

    List<ArtClassesCategoryResponseDto> toDtoList(List<ArtClassesCategory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtClassesCategoryRequestDto dto, @MappingTarget ArtClassesCategory entity);
}
