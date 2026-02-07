package com.artacademy.mapper;

import com.artacademy.dto.request.LmsClassSessionRequestDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.entity.LmsClassSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LmsClassSessionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "totalStudents", ignore = true)
    @Mapping(target = "presentCount", ignore = true)
    @Mapping(target = "absentCount", ignore = true)
    @Mapping(target = "attendanceTaken", ignore = true)
    @Mapping(target = "attendanceTakenAt", ignore = true)
    @Mapping(target = "attendanceRecords", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LmsClassSession toEntity(LmsClassSessionRequestDto dto);

    @Mapping(target = "attendanceRecords", source = "attendanceRecords")
    LmsClassSessionResponseDto toResponse(LmsClassSession entity);

    List<LmsClassSessionResponseDto> toResponseList(List<LmsClassSession> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "totalStudents", ignore = true)
    @Mapping(target = "presentCount", ignore = true)
    @Mapping(target = "absentCount", ignore = true)
    @Mapping(target = "attendanceTaken", ignore = true)
    @Mapping(target = "attendanceTakenAt", ignore = true)
    @Mapping(target = "attendanceRecords", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(LmsClassSessionRequestDto dto, @MappingTarget LmsClassSession entity);

    // Map embedded attendance record
    LmsClassSessionResponseDto.AttendanceRecordDto toAttendanceRecordDto(
            LmsClassSession.SessionAttendanceRecord record);
}
