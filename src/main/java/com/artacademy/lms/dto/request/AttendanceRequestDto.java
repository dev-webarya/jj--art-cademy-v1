package com.artacademy.lms.dto.request;

import com.artacademy.lms.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AttendanceRequestDto {

    @NotNull(message = "Batch student ID is required")
    private UUID batchStudentId;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    private String remarks;
}
