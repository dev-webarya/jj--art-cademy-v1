package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ArtGalleryRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private boolean isActive = true;

    private UUID categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}
