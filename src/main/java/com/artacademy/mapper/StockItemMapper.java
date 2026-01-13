package com.artacademy.mapper;

import com.artacademy.dto.request.StockItemRequestDto;
import com.artacademy.dto.response.StockItemResponseDto;
import com.artacademy.entity.StockItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "storeId", source = "store.id") // Will be null if store is null
    @Mapping(target = "storeName", source = "store.name") // Will be null if store is null
    StockItemResponseDto toDto(StockItem stockItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true) // Handled by service
    @Mapping(target = "store", ignore = true) // Handled by service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    StockItem toEntity(StockItemRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true) // Handled by service
    @Mapping(target = "store", ignore = true) // Handled by service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(StockItemRequestDto requestDto, @MappingTarget StockItem stockItem);
}