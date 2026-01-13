package com.artacademy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class ArtMaterialsResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Integer discount;
    private BigDecimal stock;
    private boolean isActive;
    private UUID categoryId;
    private String categoryName;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
