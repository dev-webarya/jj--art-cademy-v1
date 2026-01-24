package com.artacademy.dto.response;

import com.artacademy.enums.ClassSchedule;
import com.artacademy.enums.EnrollmentStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class ClassEnrollmentResponseDto {

    private String id;

    // Student info (from User)
    private String userId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;

    // Class info
    private String classId;
    private String className;
    private String classDescription;

    // Enrollment details
    private String parentGuardianName;
    private Integer studentAge;
    private ClassSchedule schedule;
    private String scheduleDisplayName;
    private String additionalMessage;

    // Status
    private EnrollmentStatus status;
    private String adminNotes;

    // Timestamps
    private Instant createdAt;
    private Instant updatedAt;
}
