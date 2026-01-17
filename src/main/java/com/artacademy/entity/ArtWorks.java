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
@Document(collection = "artworks")
public class ArtWorks {

        @Id
        private String id;

        @TextIndexed
        private String name;

        @TextIndexed
        private String description;

        private BigDecimal basePrice;

        @Builder.Default
        private boolean isActive = true;

        private String artistName;

        private String artMedium;

        private String size;

        @Builder.Default
        private Integer views = 0;

        @Builder.Default
        private Integer likes = 0;

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