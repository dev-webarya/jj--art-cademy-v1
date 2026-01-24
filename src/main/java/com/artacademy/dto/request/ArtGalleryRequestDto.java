package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtGalleryRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private boolean isActive = true;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}
