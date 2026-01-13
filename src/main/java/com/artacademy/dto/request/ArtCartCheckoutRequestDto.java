package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtCartCheckoutRequestDto {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    private String billingAddress;
}
