package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ArtGalleryCategoryRequestDto {

    @NotBlank(message = "Category name is required")
    private String name;

    private UUID parentId;
}
