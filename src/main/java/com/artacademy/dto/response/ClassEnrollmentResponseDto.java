package com.artacademy.dto.response;

import com.artacademy.enums.EnrollmentStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class ClassEnrollmentResponseDto {

    private String id;
    private String rollNo;
    private String userId;

    // Student Info
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String address;

    // Class Info
    private String classId;
    private String className;
    private String classDescription;

    // Enrollment Details
    private String parentGuardianName;
    private Integer studentAge;
    private String schedule;
    private String additionalMessage;

    // Emergency Contact
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Status
    private EnrollmentStatus status;
    private String adminNotes;

    // Timestamps
    private Instant createdAt;
    private Instant updatedAt;
}
