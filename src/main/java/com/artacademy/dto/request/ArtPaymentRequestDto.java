package com.artacademy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArtPaymentRequestDto {

    @NotNull(message = "Order ID is required")
    private String orderId;
}
