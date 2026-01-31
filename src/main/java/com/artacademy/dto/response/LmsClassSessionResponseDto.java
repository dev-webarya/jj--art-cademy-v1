package com.artacademy.dto.response;

import com.artacademy.enums.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Response DTO for class session data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsClassSessionResponseDto {

    private String id;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String topic;
    private String description;

    private String meetingLink;
    private String meetingPassword;

    private SessionStatus status;
    private String cancellationReason;

    // Attendance summary
    private Integer totalStudents;
    private Integer presentCount;
    private Integer absentCount;
    private Boolean attendanceTaken;
    private Instant attendanceTakenAt;

    // Embedded attendance records
    private List<AttendanceRecordDto> attendanceRecords;

    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttendanceRecordDto {
        private String studentId;
        private String studentName;
        private String studentEmail;
        private String subscriptionId;
        private Boolean isPresent;
        private Integer sessionCountThisMonth;
        private Boolean isOverLimit;
        private String remarks;
        private Instant markedAt;
    }
}
