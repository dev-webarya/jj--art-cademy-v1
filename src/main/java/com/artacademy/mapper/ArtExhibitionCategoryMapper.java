package com.artacademy.mapper;

import com.artacademy.dto.request.ArtExhibitionCategoryRequestDto;
import com.artacademy.dto.response.ArtExhibitionCategoryResponseDto;
import com.artacademy.entity.ArtExhibitionCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtExhibitionCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtExhibitionCategory toEntity(ArtExhibitionCategoryRequestDto dto);

    @Mapping(target = "parentId", source = "parent.categoryId")
    @Mapping(target = "parentName", source = "parent.name")
    ArtExhibitionCategoryResponseDto toDto(ArtExhibitionCategory entity);

    List<ArtExhibitionCategoryResponseDto> toDtoList(List<ArtExhibitionCategory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtExhibitionCategoryRequestDto dto, @MappingTarget ArtExhibitionCategory entity);
}
