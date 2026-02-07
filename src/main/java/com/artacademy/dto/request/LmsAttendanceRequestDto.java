package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for marking attendance.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsAttendanceRequestDto {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotEmpty(message = "At least one attendance record is required")
    private List<StudentAttendanceDto> attendanceList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentAttendanceDto {
        @NotBlank(message = "Student ID is required")
        private String studentId;

        @NotNull(message = "Presence status is required")
        private Boolean isPresent;

        private String remarks;
    }
}
