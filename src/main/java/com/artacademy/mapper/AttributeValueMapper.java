package com.artacademy.mapper;

import com.artacademy.dto.request.AttributeValueRequestDto;
import com.artacademy.dto.response.AttributeValueResponseDto;
import com.artacademy.entity.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttributeValueMapper {

    @Mapping(target = "attributeTypeId", source = "attributeType.id")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    AttributeValueResponseDto toDto(AttributeValue attributeValue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attributeType", ignore = true) // Handled by service
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AttributeValue toEntity(AttributeValueRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attributeType", ignore = true) // Handled by service
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(AttributeValueRequestDto requestDto, @MappingTarget AttributeValue attributeValue);
}