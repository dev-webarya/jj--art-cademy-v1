package com.artacademy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ArtClassesRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal basePrice;

    private BigDecimal discountPrice;

    private Integer durationWeeks;

    @NotBlank(message = "Proficiency level is required")
    private String proficiency;

    private boolean isActive = true;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}
