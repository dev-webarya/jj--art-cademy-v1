package com.artacademy.mapper;

import com.artacademy.dto.response.LmsAttendanceResponseDto;
import com.artacademy.entity.LmsAttendance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LmsAttendanceMapper {

    LmsAttendanceResponseDto toResponse(LmsAttendance entity);

    List<LmsAttendanceResponseDto> toResponseList(List<LmsAttendance> entities);
}
