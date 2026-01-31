package com.artacademy.mapper;

import com.artacademy.dto.request.LmsSubscriptionRequestDto;
import com.artacademy.dto.response.LmsSubscriptionResponseDto;
import com.artacademy.entity.LmsStudentSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LmsSubscriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentName", ignore = true)
    @Mapping(target = "studentEmail", ignore = true)
    @Mapping(target = "studentPhone", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "attendedSessions", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LmsStudentSubscription toEntity(LmsSubscriptionRequestDto dto);

    @Mapping(target = "remainingSessions", expression = "java(entity.getRemainingSessionCount())")
    @Mapping(target = "isOverLimit", expression = "java(entity.isOverLimit())")
    LmsSubscriptionResponseDto toResponse(LmsStudentSubscription entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "studentName", ignore = true)
    @Mapping(target = "studentEmail", ignore = true)
    @Mapping(target = "studentPhone", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "attendedSessions", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(LmsSubscriptionRequestDto dto, @MappingTarget LmsStudentSubscription entity);
}
