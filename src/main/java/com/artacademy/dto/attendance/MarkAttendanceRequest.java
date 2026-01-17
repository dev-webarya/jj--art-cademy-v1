package com.artacademy.dto.attendance;

import com.artacademy.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkAttendanceRequest {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Attendance status is required")
    private AttendanceStatus status;

    private Instant checkInTime;
    private Instant checkOutTime;

    private String remarks;
}
