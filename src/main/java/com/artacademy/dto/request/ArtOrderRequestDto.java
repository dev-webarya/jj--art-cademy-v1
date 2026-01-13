package com.artacademy.dto.request;

import com.artacademy.enums.ArtItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ArtOrderRequestDto {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    private String billingAddress;

    @NotNull(message = "Order items are required")
    private List<ArtOrderItemDto> items;

    @Data
    public static class ArtOrderItemDto {
        @NotNull(message = "Item ID is required")
        private UUID itemId;

        @NotNull(message = "Item type is required")
        private ArtItemType itemType;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}
