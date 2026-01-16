package com.artacademy.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AssignmentRequestDto {

    @NotNull(message = "Batch ID is required")
    private UUID batchId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String instructions;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    private Integer maxScore = 100;

    // Google Drive links for resources
    private String resourceLinks;
}
