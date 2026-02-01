package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtGalleryCategoryRequestDto {

    @NotBlank(message = "Category name is required")
    private String name;
}
