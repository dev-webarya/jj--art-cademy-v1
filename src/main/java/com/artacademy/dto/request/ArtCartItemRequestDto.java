package com.artacademy.dto.request;

import com.artacademy.enums.ArtItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArtCartItemRequestDto {

    @NotNull(message = "Item ID is required")
    private String itemId;

    @NotNull(message = "Item type is required")
    private ArtItemType itemType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
