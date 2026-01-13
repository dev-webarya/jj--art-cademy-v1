package com.artacademy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class ArtWorksResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private boolean isActive;
    private String artistName;
    private String artMedium;
    private String size;
    private Integer views;
    private Integer likes;
    private UUID categoryId;
    private String categoryName;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
