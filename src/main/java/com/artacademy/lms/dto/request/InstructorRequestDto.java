package com.artacademy.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class InstructorRequestDto {

    private UUID userId; // For admin to assign instructor to existing user

    @NotBlank(message = "Bio is required")
    private String bio;

    private String specialization;

    private String qualification;

    private String profileImageUrl;

    private boolean isActive = true;
}
