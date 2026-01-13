package com.artacademy.service;

import com.artacademy.dto.request.ShipmentRequestDto;
import com.artacademy.dto.response.ShipmentResponseDto;
import com.artacademy.enums.ShipmentStatus;

import java.util.UUID;

public interface ShipmentService {

    // Create a shipment label/record
    ShipmentResponseDto createShipment(ShipmentRequestDto request);

    // Update status (e.g., from Webhook or manual scan)
    ShipmentResponseDto updateShipmentStatus(UUID shipmentId, ShipmentStatus status);

    ShipmentResponseDto getShipmentByOrderId(UUID orderId);
}