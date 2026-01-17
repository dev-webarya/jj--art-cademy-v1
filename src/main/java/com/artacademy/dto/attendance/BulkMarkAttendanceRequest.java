package com.artacademy.dto.attendance;

import com.artacademy.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkMarkAttendanceRequest {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotEmpty(message = "At least one attendance record is required")
    private List<StudentAttendance> attendances;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentAttendance {
        private String studentId;
        private AttendanceStatus status;
        private String remarks;
    }
}
