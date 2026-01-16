package com.artacademy.dto.request;

import com.artacademy.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollmentStatusUpdateDto {

    @NotNull(message = "Status is required")
    private EnrollmentStatus status;

    private String adminNotes;
}
