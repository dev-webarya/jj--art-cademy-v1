package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "art_exhibitions")
public class ArtExhibition {

        @Id
        private String id;

        private String name;

        private String description;

        @Builder.Default
        private boolean isActive = true;

        @Builder.Default
        private boolean deleted = false;

        // Embedded category reference
        private CategoryRef category;

        @CreatedDate
        private Instant createdAt;

        @LastModifiedDate
        private Instant updatedAt;

        private String imageUrl;

        @Indexed
        private LocalDate startDate;

        private LocalDate endDate;

        private String location;

        @Builder.Default
        private Integer artistCount = 0;

        @Builder.Default
        private Integer artworksCount = 0;

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