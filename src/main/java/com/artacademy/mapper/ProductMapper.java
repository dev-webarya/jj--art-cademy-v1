package com.artacademy.mapper;

import com.artacademy.dto.request.ProductRequestDto;
import com.artacademy.dto.response.ProductResponseDto;
import com.artacademy.entity.AttributeValue;
import com.artacademy.entity.Collection;
import com.artacademy.entity.Product;
import com.artacademy.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        CategoryMapper.class } // We can reuse the CategoryMapper
)
public interface ProductMapper {

    // --- Entity to DTO ---

    // This is the main mapping
    @Mapping(target = "category", source = "category") // Uses CategoryMapper
    @Mapping(target = "collections", source = "collections")
    @Mapping(target = "attributes", source = "attributes")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "active", source = "active") // Correct for getter isActive()
    ProductResponseDto toDto(Product product);

    // Map nested entities to their simple DTOs
    ProductResponseDto.SimpleCollectionDto collectionToSimpleCollectionDto(Collection collection);

    @Mapping(target = "primary", source = "primary") // Correct for getter isPrimary()
    ProductResponseDto.SimpleProductImageDto productImageToSimpleProductImageDto(ProductImage productImage);

    @Mapping(target = "attributeTypeId", source = "attributeType.id")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    ProductResponseDto.SimpleAttributeValueDto attributeValueToSimpleAttributeValueDto(AttributeValue attributeValue);

    // --- DTO to Entity ---

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true) // Handled by service
    @Mapping(target = "collections", ignore = true) // Handled by service
    @Mapping(target = "attributes", ignore = true) // Handled by service
    @Mapping(target = "images", ignore = true)
    // FIX: The builder method is isActive(), so target must be isActive
    @Mapping(target = "isActive", source = "active")
    Product toEntity(ProductRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true) // Handled by service
    @Mapping(target = "collections", ignore = true) // Handled by service
    @Mapping(target = "attributes", ignore = true) // Handled by service
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "active", source = "active") // Correct for setter setActive()
    void updateEntityFromDto(ProductRequestDto requestDto, @MappingTarget Product product);
}