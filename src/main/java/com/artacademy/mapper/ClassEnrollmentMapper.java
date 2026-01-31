package com.artacademy.mapper;

import com.artacademy.dto.request.ClassEnrollmentRequestDto;
import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.ClassEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassEnrollmentMapper {

    // Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true) // Set by Service
    @Mapping(target = "className", ignore = true) // Set by Service
    @Mapping(target = "classDescription", ignore = true) // Set by Service
    @Mapping(target = "status", ignore = true) // Default PENDING
    @Mapping(target = "adminNotes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClassEnrollment toEntity(ClassEnrollmentRequestDto request);

    // Entity -> Response
    ClassEnrollmentResponseDto toDto(ClassEnrollment enrollment);

    List<ClassEnrollmentResponseDto> toDtoList(List<ClassEnrollment> enrollments);
}
