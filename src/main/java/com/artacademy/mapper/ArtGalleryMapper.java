package com.artacademy.mapper;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.entity.ArtGallery;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtGalleryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtGallery toEntity(ArtGalleryRequestDto dto);

    @Mapping(target = "categoryName", ignore = true) // Would need separate category lookup
    ArtGalleryResponseDto toDto(ArtGallery entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtGalleryRequestDto dto, @MappingTarget ArtGallery entity);
}
