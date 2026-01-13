package com.artacademy.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ArtGalleryResponseDto {
    private UUID id;
    private String name;
    private String description;
    private boolean isActive;
    private UUID categoryId;
    private String categoryName;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
