package com.artacademy.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class ArtGalleryResponseDto {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
    private String categoryId;
    private String categoryName;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
