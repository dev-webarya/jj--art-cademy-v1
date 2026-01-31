package com.artacademy.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtMaterialsRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private BigDecimal basePrice;

    private Integer discount;

    private BigDecimal stock;

    @Valid
    private List<MaterialVariantRequestDto> variants;

    private boolean isActive = true;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}