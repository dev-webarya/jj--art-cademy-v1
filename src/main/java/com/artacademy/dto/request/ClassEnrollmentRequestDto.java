package com.artacademy.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClassEnrollmentRequestDto {

    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Student email is required")
    @Email(message = "Invalid email format")
    private String studentEmail;

    @NotBlank(message = "Student phone is required")
    private String studentPhone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Parent/Guardian name is required")
    private String parentGuardianName;

    @NotNull(message = "Student age is required")
    @Min(value = 3, message = "Student must be at least 3 years old")
    @Max(value = 100, message = "Invalid age")
    private Integer studentAge;

    @NotBlank(message = "Schedule is required")
    private String schedule;

    private String additionalMessage;

    // Emergency Contact
    @NotBlank(message = "Emergency contact name is required")
    private String emergencyContactName;

    @NotBlank(message = "Emergency contact phone is required")
    private String emergencyContactPhone;
}
