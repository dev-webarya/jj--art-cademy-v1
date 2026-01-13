package com.artacademy.dto.response;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class CategoryResponseDto {

    private UUID id;
    private String name;
    private UUID parentId;

    // Include subcategories in the response to show the hierarchy
    private Set<CategoryResponseDto> subcategories;
}