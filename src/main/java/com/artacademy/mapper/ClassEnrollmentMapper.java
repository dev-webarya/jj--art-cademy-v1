package com.artacademy.mapper;

import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.ClassEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassEnrollmentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "studentName", expression = "java(enrollment.getUser().getFirstName() + \" \" + enrollment.getUser().getLastName())")
    @Mapping(target = "studentEmail", source = "user.email")
    @Mapping(target = "studentPhone", source = "user.phoneNumber")
    @Mapping(target = "classId", source = "artClass.id")
    @Mapping(target = "className", source = "artClass.name")
    @Mapping(target = "classDescription", source = "artClass.description")
    @Mapping(target = "scheduleDisplayName", expression = "java(enrollment.getSchedule().getDisplayName())")
    ClassEnrollmentResponseDto toResponseDto(ClassEnrollment enrollment);

    List<ClassEnrollmentResponseDto> toResponseDtoList(List<ClassEnrollment> enrollments);
}
