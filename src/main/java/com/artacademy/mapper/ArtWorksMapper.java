package com.artacademy.mapper;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import com.artacademy.entity.ArtWorks;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtWorksMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true) // Handled manually in Service

    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtWorks toEntity(ArtWorksRequestDto dto);

    ArtWorksResponseDto toDto(ArtWorks entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true) // Handled manually in Service

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtWorksRequestDto dto, @MappingTarget ArtWorks entity);
}
