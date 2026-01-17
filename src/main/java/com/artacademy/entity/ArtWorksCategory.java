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
@Document(collection = "art_works_categories")
public class ArtWorksCategory {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    private String imageUrl;

    // Parent reference for hierarchy
    private CategoryRef parent;

    @Builder.Default
    private boolean deleted = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

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