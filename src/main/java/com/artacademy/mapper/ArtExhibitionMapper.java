package com.artacademy.mapper;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import com.artacademy.entity.ArtExhibition;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtExhibitionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artExhibitionCategory", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtExhibition toEntity(ArtExhibitionRequestDto dto);

    @Mapping(target = "categoryId", source = "artExhibitionCategory.id")
    @Mapping(target = "categoryName", source = "artExhibitionCategory.name")
    ArtExhibitionResponseDto toDto(ArtExhibition entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artExhibitionCategory", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArtExhibitionRequestDto dto, @MappingTarget ArtExhibition entity);
}
