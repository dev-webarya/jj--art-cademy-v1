package com.artacademy.dto.response;

import com.artacademy.enums.VerificationStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class ArtGalleryResponseDto {
    private String id;
    private String name;
    private String description;
    private VerificationStatus status;
    private String categoryId;
    private String categoryName;
    private String imageUrl;
    private String userId;
    private String userName;
    private Instant createdAt;
    private Instant updatedAt;
}
