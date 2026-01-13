package com.artacademy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class ArtClassesResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private boolean isActive;
    private String proficiency;
    private UUID categoryId;
    private String categoryName;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
