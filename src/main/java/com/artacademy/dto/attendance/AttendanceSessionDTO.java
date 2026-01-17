package com.artacademy.dto.attendance;

import com.artacademy.enums.SessionStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSessionDTO {
    private String id;
    private String sessionCode;

    // Art class info
    private String classId;
    private String className;
    private String proficiency;

    // Teacher info
    private String teacherId;
    private String teacherName;
    private String teacherEmail;

    private LocalDate sessionDate;
    private String startTime;
    private String endTime;
    private SessionStatus status;

    private Integer totalEnrolled;
    private Integer totalPresent;
    private Integer totalAbsent;

    private String remarks;
    private Instant createdAt;
    private Instant updatedAt;
}
