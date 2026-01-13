package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CartCheckoutRequestDto {
    // Optional: Only sent if user wants Pickup. Null = Delivery.
    private UUID fulfillmentStoreId;

    // Required only if fulfillmentStoreId is null (Delivery)
    private String shippingAddress;

    @NotBlank(message = "Billing address cannot be blank")
    private String billingAddress;
}