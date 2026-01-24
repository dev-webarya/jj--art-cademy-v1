package com.artacademy.mapper;

import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.ClassEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassEnrollmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userEmail", ignore = true)
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "classDescription", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNotes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClassEnrollment toEntity(com.artacademy.dto.request.ClassEnrollmentRequestDto request);

    @Mapping(target = "studentEmail", source = "userEmail")
    @Mapping(target = "studentPhone", ignore = true) // Not in entity
    @Mapping(target = "scheduleDisplayName", expression = "java(enrollment.getSchedule() != null ? enrollment.getSchedule().getDisplayName() : null)")
    ClassEnrollmentResponseDto toDto(ClassEnrollment enrollment);

    List<ClassEnrollmentResponseDto> toDtoList(List<ClassEnrollment> enrollments);
}
