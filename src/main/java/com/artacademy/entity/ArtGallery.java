package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "galleries")
public class ArtGallery {

        @Id
        private String id;

        private String name;

        private String description;

        @Builder.Default
        private boolean isActive = true;

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