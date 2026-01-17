package com.artacademy.lms.dto.response;

import com.artacademy.lms.enums.AssignmentStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AssignmentResponseDto {

    private UUID id;
    private UUID batchId;
    private String batchName;
    private String title;
    private String description;
    private String instructions;
    private LocalDateTime dueDate;
    private Integer maxScore;
    private String resourceLinks;
    private AssignmentStatus status;
    private String createdByName;
    private int submissionCount;
    private int gradedCount;
    private Instant createdAt;
    private Instant updatedAt;
}
