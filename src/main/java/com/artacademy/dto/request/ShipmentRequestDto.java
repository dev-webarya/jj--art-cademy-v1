package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class ShipmentRequestDto {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotBlank(message = "Carrier is required")
    private String carrier; // e.g., "FedEx", "DHL", "BlueDart", "India Post"

    private String trackingUrl; // Optional: direct link to carrier tracking page

    private Instant estimatedDelivery; // Optional: estimated delivery date
}
