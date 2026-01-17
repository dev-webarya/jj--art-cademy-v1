package com.artacademy.dto.attendance;

import com.artacademy.enums.AttendanceStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordDTO {
    private String id;

    // Session info
    private String sessionId;
    private String sessionCode;
    private LocalDate sessionDate;

    // Class info
    private String classId;
    private String className;

    // Student info
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;

    private AttendanceStatus status;
    private Instant checkInTime;
    private Instant checkOutTime;

    // Marked by info
    private String markedById;
    private String markedByName;
    private String markedByRole;

    private String remarks;
    private Instant createdAt;
    private Instant updatedAt;
}
