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
@Document(collection = "classes")
public class ArtClasses {

        @Id
        private String id;

        @TextIndexed
        private String name;

        @TextIndexed
        private String description;

        private BigDecimal basePrice;

        private BigDecimal discountPrice;

        private Integer durationWeeks;

        @Builder.Default
        private boolean isActive = true;

        private String proficiency; // e.g. Beginner, Intermediate, Advanced

        // Soft Delete Flag
        @Builder.Default
        private boolean deleted = false;

        @CreatedDate
        private Instant createdAt;

        @LastModifiedDate
        private Instant updatedAt;

        @Indexed
        private String categoryId;

        private String categoryName;

        private String imageUrl;
}