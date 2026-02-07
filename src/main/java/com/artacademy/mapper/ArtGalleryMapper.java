package com.artacademy.mapper;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.entity.ArtGallery;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtGalleryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userName", ignore = true)
    ArtGallery toEntity(ArtGalleryRequestDto dto);

    ArtGalleryResponseDto toDto(ArtGallery entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userName", ignore = true)
    void updateEntity(ArtGalleryRequestDto dto, @MappingTarget ArtGallery entity);
}
