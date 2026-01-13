package com.artacademy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ArtPaymentRequestDto {

    @NotNull(message = "Order ID is required")
    private UUID orderId;
}
