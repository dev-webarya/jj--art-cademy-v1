package com.artacademy.mapper;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.entity.ArtClasses;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtClassesMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true) // Handled manually in Service
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtClasses toEntity(ArtClassesRequestDto dto);

    ArtClassesResponseDto toDto(ArtClasses entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true) // Handled manually in Service
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtClassesRequestDto dto, @MappingTarget ArtClasses entity);
}
