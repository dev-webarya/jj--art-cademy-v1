package com.artacademy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtMaterialsCategoryResponseDto {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String parentId;
    private String parentName;
    private Instant createdAt;
    private Instant updatedAt;
}
