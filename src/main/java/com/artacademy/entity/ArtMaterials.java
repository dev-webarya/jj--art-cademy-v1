package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "materials")
public class ArtMaterials {

    @Id
    private String id;

    @TextIndexed
    private String name;

    @TextIndexed
    private String description;

    private BigDecimal basePrice;

    @Builder.Default
    private Integer discount = 0;

    private BigDecimal stock; // count for item

    @Builder.Default
    private java.util.List<MaterialVariant> variants = new java.util.ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialVariant {
        private String id;
        private String size; // e.g. "A4", "10x12 inches"
        private BigDecimal price;
        private BigDecimal discountPrice;
        private BigDecimal stock;
    }

    @Builder.Default
    private boolean isActive = true;

    // Soft Delete Flag
    @Builder.Default
    private boolean deleted = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Store category ID reference instead of @ManyToOne
    @Indexed
    private String categoryId;

    private String categoryName;

    private String imageUrl;
}