package com.artacademy.controller;

import com.artacademy.dto.request.ShipmentRequestDto;
import com.artacademy.dto.response.ShipmentResponseDto;
import com.artacademy.enums.ShipmentStatus;
import com.artacademy.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Slf4j
public class ShipmentController {

    private final ShipmentService shipmentService;

    // Only Admin/Manager can create shipments (generate labels)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<ShipmentResponseDto> createShipment(@Valid @RequestBody ShipmentRequestDto request) {
        log.info("Creating shipment for order: {} with carrier: {}", request.getOrderId(), request.getCarrier());
        return new ResponseEntity<>(shipmentService.createShipment(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<ShipmentResponseDto> updateStatus(@PathVariable UUID id,
            @RequestParam ShipmentStatus status) {
        log.info("Updating shipment {} status to: {}", id, status);
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, status));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponseDto> getShipmentByOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }
}