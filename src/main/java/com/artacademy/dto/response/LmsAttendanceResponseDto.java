package com.artacademy.dto.response;

import com.artacademy.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Response DTO for attendance data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsAttendanceResponseDto {

    private String id;
    private String sessionId;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String subscriptionId;

    private LocalDate sessionDate;
    private Integer sessionMonth;
    private Integer sessionYear;

    private Integer sessionCountThisMonth;
    private Boolean isOverLimit;

    private AttendanceStatus status;
    private Instant markedAt;
    private String remarks;

    private Instant createdAt;
}
