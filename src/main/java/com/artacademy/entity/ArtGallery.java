package com.artacademy.entity;

import com.artacademy.enums.VerificationStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field("status")
    private VerificationStatus status = VerificationStatus.PENDING;

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

    @Indexed
    @Field("user_id")
    private String userId;

    @Field("user_name")
    private String userName;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}