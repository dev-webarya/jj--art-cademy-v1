package com.artacademy.mapper;

import com.artacademy.dto.request.AttributeTypeRequestDto;
import com.artacademy.dto.response.AttributeTypeResponseDto;
import com.artacademy.entity.AttributeType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttributeTypeMapper {

    AttributeTypeResponseDto toDto(AttributeType attributeType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "values", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AttributeType toEntity(AttributeTypeRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "values", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(AttributeTypeRequestDto requestDto, @MappingTarget AttributeType attributeType);
}