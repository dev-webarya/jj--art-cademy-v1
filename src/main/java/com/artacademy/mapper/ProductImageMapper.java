package com.artacademy.mapper;

import com.artacademy.dto.request.ProductImageRequestDto;
import com.artacademy.dto.response.ProductImageResponseDto;
import com.artacademy.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductImageMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "primary", source = "primary") // Correct for getter isPrimary()
    ProductImageResponseDto toDto(ProductImage productImage);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true) // Handled by service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // FIX: The builder method is isPrimary(), so target must be isPrimary
    @Mapping(target = "isPrimary", source = "primary")
    ProductImage toEntity(ProductImageRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true) // Handled by service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "primary", source = "primary") // Correct for setter setPrimary()
    void updateEntityFromDto(ProductImageRequestDto requestDto, @MappingTarget ProductImage productImage);
}