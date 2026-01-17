package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "art_galleries")
public class ArtGallery {

        @Id
        private String id;

        private String name;

        private String description;

        @Builder.Default
        private boolean isActive = true;

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