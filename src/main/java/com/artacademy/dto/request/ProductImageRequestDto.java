package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class ProductImageRequestDto {

    @NotNull(message = "Product ID cannot be null")
    private UUID productId;

    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl; // In a real app, this might be a file upload

    private boolean isPrimary = false;
}