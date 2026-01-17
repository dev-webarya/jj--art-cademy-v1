package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "art_works")
public class ArtWorks {

        @Id
        private String id;

        @Indexed
        private String name;

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

        @Builder.Default
        private boolean deleted = false;

        @CreatedDate
        private Instant createdAt;

        @LastModifiedDate
        private Instant updatedAt;

        // Embedded category reference
        private CategoryRef category;

        private String imageUrl;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CategoryRef {
                private String categoryId;
                private String name;
        }
}