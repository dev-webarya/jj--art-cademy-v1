package com.artacademy.dto.attendance;

import com.artacademy.enums.EnrollmentStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassEnrollmentDTO {
    private String id;

    // Student info
    private String studentId;
    private String studentName;
    private String studentEmail;

    // Class info
    private String classId;
    private String className;
    private String proficiency;

    private LocalDate enrollmentDate;
    private EnrollmentStatus status;
    private String paymentStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Instant createdAt;
    private Instant updatedAt;
}
