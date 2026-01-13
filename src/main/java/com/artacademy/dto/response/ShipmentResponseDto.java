package com.artacademy.dto.response;

import com.artacademy.enums.ShipmentStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ShipmentResponseDto {
    private UUID id;
    private UUID orderId;
    private String trackingNumber;
    private String carrier;
    private ShipmentStatus status;
    private Instant createdAt;
}