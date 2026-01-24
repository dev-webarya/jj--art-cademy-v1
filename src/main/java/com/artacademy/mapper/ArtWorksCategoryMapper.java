package com.artacademy.mapper;

import com.artacademy.dto.request.ArtWorksCategoryRequestDto;
import com.artacademy.dto.response.ArtWorksCategoryResponseDto;
import com.artacademy.entity.ArtWorksCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtWorksCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtWorksCategory toEntity(ArtWorksCategoryRequestDto dto);

    ArtWorksCategoryResponseDto toDto(ArtWorksCategory entity);

    List<ArtWorksCategoryResponseDto> toDtoList(List<ArtWorksCategory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtWorksCategoryRequestDto dto, @MappingTarget ArtWorksCategory entity);
}
