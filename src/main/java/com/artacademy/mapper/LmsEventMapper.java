package com.artacademy.mapper;

import com.artacademy.dto.request.LmsEventRequestDto;
import com.artacademy.dto.response.LmsEventResponseDto;
import com.artacademy.entity.LmsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LmsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentParticipants", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LmsEvent toEntity(LmsEventRequestDto dto);

    @Mapping(target = "availableSlots", expression = "java(entity.getMaxParticipants() != null ? entity.getMaxParticipants() - entity.getCurrentParticipants() : null)")
    @Mapping(target = "hasAvailableSlots", expression = "java(entity.hasAvailableSlots())")
    LmsEventResponseDto toResponse(LmsEvent entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentParticipants", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(LmsEventRequestDto dto, @MappingTarget LmsEvent entity);
}
