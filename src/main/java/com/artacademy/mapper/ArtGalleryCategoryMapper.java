package com.artacademy.mapper;

import com.artacademy.dto.request.ArtGalleryCategoryRequestDto;
import com.artacademy.dto.response.ArtGalleryCategoryResponseDto;
import com.artacademy.entity.ArtGalleryCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtGalleryCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtGalleryCategory toEntity(ArtGalleryCategoryRequestDto dto);

    ArtGalleryCategoryResponseDto toDto(ArtGalleryCategory entity);

    List<ArtGalleryCategoryResponseDto> toDtoList(List<ArtGalleryCategory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtGalleryCategoryRequestDto dto, @MappingTarget ArtGalleryCategory entity);
}
