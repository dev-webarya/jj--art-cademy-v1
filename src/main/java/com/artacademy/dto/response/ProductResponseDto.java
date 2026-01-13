package com.artacademy.dto.response;

import com.artacademy.entity.Category;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class ProductResponseDto {

    private UUID id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    // --- Enriched Relationship Data ---

    // Embedding the full Category DTO for detail
    private CategoryResponseDto category;

    // Embedding a lightweight DTO for collections
    private Set<SimpleCollectionDto> collections;

    // Embedding a lightweight DTO for attributes
    private Set<SimpleAttributeValueDto> attributes;

    // Embedding a lightweight DTO for images
    private Set<SimpleProductImageDto> images;

    // --- Lightweight nested DTOs ---
    @Data
    public static class SimpleCollectionDto {
        private UUID id;
        private String name;
    }

    @Data
    public static class SimpleAttributeValueDto {
        private UUID id;
        private String value;
        private UUID attributeTypeId;
        private String attributeTypeName;
    }

    @Data
    public static class SimpleProductImageDto {
        private UUID id;
        private String imageUrl;
        private boolean isPrimary;
    }
}
