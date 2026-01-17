package com.artacademy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtWorksCategoryResponseDto {

    private String id;
    private String name;
    private String parentId;
    private String parentName;
    private List<ArtWorksCategoryResponseDto> subcategories;
    private Instant createdAt;
    private Instant updatedAt;
}
