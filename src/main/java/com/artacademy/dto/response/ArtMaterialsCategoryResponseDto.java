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
public class ArtMaterialsCategoryResponseDto {

    private UUID id;
    private String name;
    private UUID parentId;
    private String parentName;
    private List<ArtMaterialsCategoryResponseDto> subcategories;
    private Instant createdAt;
    private Instant updatedAt;
}
