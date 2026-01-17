package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtMaterialsCategoryRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private String imageUrl;
    private String parentId;
}
