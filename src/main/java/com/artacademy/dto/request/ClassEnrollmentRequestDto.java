package com.artacademy.dto.request;

import com.artacademy.enums.ClassSchedule;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class ClassEnrollmentRequestDto {

    @NotNull(message = "Class ID is required")
    private UUID classId;

    @NotBlank(message = "Parent/Guardian name is required")
    private String parentGuardianName;

    @NotNull(message = "Student age is required")
    @Min(value = 5, message = "Student must be at least 5 years old")
    @Max(value = 100, message = "Invalid age")
    private Integer studentAge;

    @NotNull(message = "Schedule is required")
    private ClassSchedule schedule;

    private String additionalMessage;
}
