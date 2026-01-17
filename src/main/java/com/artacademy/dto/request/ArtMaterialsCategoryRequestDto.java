package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtMaterialsCategoryRequestDto {

    @NotBlank(message = "Category name is required")
    private String name;

    private String parentId;
}
