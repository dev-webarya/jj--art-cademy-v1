package com.artacademy.lms.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class InstructorResponseDto {

    private UUID id;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String bio;
    private String specialization;
    private String qualification;
    private String profileImageUrl;
    private boolean isActive;
    private int batchCount;
    private Instant createdAt;
    private Instant updatedAt;
}
