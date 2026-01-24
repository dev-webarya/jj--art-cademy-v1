package com.artacademy.lms.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class BatchStudentResponseDto {

    private UUID id;
    private UUID batchId;
    private String batchName;
    private UUID userId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private boolean isActive;
    private Instant joinedAt;
}
