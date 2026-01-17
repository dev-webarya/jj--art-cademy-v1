package com.artacademy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ArtWorksRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal basePrice;

    @NotBlank(message = "Artist name is required")
    private String artistName;

    @NotBlank(message = "Art medium is required")
    private String artMedium;

    @NotBlank(message = "Size is required")
    private String size;

    private boolean isActive = true;

    private String categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}
