package com.artacademy.dto.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollStudentRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private String paymentStatus; // "PAID", "PENDING", "PARTIAL"
}
