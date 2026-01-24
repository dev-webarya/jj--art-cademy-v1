package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exhibitions")
public class ArtExhibition {

    @Id
    private String id;

    private String name;

    private String description;

    @Builder.Default
    @Field("is_active")
    private boolean isActive = true;

    // Soft Delete Flag
    @Builder.Default
    private boolean deleted = false;

    @Indexed
    @Field("category_id")
    private String categoryId;

    @Field("category_name")
    private String categoryName;

    @Field("image_url")
    private String imageUrl;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    private String location;

    @Builder.Default
    @Field("artist_count")
    private Integer artistCount = 0;

    @Builder.Default
    @Field("artworks_count")
    private Integer artworksCount = 0;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}