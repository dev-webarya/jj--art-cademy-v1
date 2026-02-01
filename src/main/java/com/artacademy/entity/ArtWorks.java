package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    @Field("base_price")
    private BigDecimal basePrice;

    @Field("discount_price")
    private BigDecimal discountPrice;

    @Builder.Default
    @Field("is_active")
    private boolean isActive = true;

    @Field("artist_name")
    private String artistName;

    @Field("art_medium")
    private String artMedium;

    private String size;

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

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}