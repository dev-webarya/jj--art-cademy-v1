package com.artacademy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class ProductRequestDto {

    @NotBlank(message = "SKU cannot be blank")
    @Size(max = 100)
    private String sku;

    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull(message = "Base price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be positive")
    private BigDecimal basePrice;

    private boolean isActive = true;

    // --- Relationships ---

    private UUID categoryId; // Can be null

    // Set of IDs for relationships
    private Set<UUID> collectionIds;
    private Set<UUID> attributeValueIds;

    // Note: ProductImages are handled via their own endpoint
}
