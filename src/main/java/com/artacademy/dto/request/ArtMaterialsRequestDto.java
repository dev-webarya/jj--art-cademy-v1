package com.artacademy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ArtMaterialsRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal basePrice;

    @NotNull(message = "Discount is required")
    @Min(value = 0, message = "Discount cannot be negative")
    private Integer discount;

    @NotNull(message = "Stock is required")
    @DecimalMin(value = "0.0", message = "Stock cannot be negative")
    private BigDecimal stock;

    private boolean isActive = true;

    private String categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}
