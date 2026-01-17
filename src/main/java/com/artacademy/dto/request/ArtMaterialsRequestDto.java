package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ArtMaterialsRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    // Base price is now optional or used as default/display price
    // @NotNull(message = "Base price is required")
    // Commented out NotNull to allow empty basePrice if variants are present, but
    // validation logic might be needed in service or custom validator.
    // For now, keeping legacy fields but adding variants.
    private BigDecimal basePrice;

    private Integer discount;

    private BigDecimal stock;

    private java.util.List<MaterialVariantRequestDto> variants;

    private boolean isActive = true;

    private String categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}
