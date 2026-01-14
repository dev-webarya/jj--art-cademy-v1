package com.artacademy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtGalleryCategoryResponseDto {

    private UUID id;
    private String name;
    private UUID parentId;
    private String parentName;
    private List<ArtGalleryCategoryResponseDto> subcategories;
    private Instant createdAt;
    private Instant updatedAt;
}
