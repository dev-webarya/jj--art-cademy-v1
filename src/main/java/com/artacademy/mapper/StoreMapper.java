package com.artacademy.mapper;

import com.artacademy.dto.request.StoreRequestDto;
import com.artacademy.dto.response.StoreResponseDto;
import com.artacademy.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StoreMapper {

    StoreResponseDto toDto(Store store);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stockItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Store toEntity(StoreRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stockItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(StoreRequestDto requestDto, @MappingTarget Store store);
}