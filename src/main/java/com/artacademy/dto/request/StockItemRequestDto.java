package com.artacademy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class StockItemRequestDto {

    @NotNull(message = "Product ID cannot be null")
    private UUID productId;

    // Can be null, indicating central warehouse
    private UUID storeId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}