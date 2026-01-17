package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtPaymentRequestDto {

    @NotBlank(message = "Order ID is required")
    private String orderId;
}
