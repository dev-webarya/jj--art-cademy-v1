package com.artacademy.controller;

import com.artacademy.dto.request.ArtCartCheckoutRequestDto;
import com.artacademy.dto.request.ArtOrderRequestDto;
import com.artacademy.dto.request.ShipmentRequestDto;
import com.artacademy.dto.response.ArtOrderResponseDto;
import com.artacademy.enums.OrderStatus;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.service.ArtOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/art-orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Art Orders", description = "Order management endpoints")
public class ArtOrderController {

    private final ArtOrderService artOrderService;

    @PostMapping
    @Operation(summary = "Create an order directly")
    public ResponseEntity<ArtOrderResponseDto> createOrder(@Valid @RequestBody ArtOrderRequestDto request) {
        log.info("Creating order with {} items", request.getItems().size());
        return new ResponseEntity<>(artOrderService.createOrder(request), HttpStatus.CREATED);
    }

    @PostMapping("/checkout")
    @Operation(summary = "Checkout cart to create an order")
    public ResponseEntity<ArtOrderResponseDto> checkoutCart(@Valid @RequestBody ArtCartCheckoutRequestDto request) {
        log.info("Checking out cart");
        return new ResponseEntity<>(artOrderService.checkoutCart(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ArtOrderResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artOrderService.getById(id));
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Get current user's orders")
    public ResponseEntity<Page<ArtOrderResponseDto>> getMyOrders(Pageable pageable) {
        return ResponseEntity.ok(artOrderService.getMyOrders(pageable));
    }

    @GetMapping
    @ManagerAccess
    @Operation(summary = "Get all orders (Admin/Manager)")
    public ResponseEntity<Page<ArtOrderResponseDto>> getAll(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String orderNumber,
            Pageable pageable) {
        return ResponseEntity.ok(artOrderService.getAll(status, orderNumber, null, null, null, null, null, pageable));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update order status (Admin/Manager)")
    public ResponseEntity<ArtOrderResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String notes) {
        log.info("Updating order {} status to: {}", id, status);
        return ResponseEntity.ok(artOrderService.updateStatus(id, status, notes));
    }

    @PostMapping("/{id}/ship")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Ship an order with tracking info (Admin/Manager)")
    public ResponseEntity<ArtOrderResponseDto> shipOrder(
            @PathVariable String id,
            @Valid @RequestBody ShipmentRequestDto shipmentRequest) {
        log.info("Shipping order {} via {}", id, shipmentRequest.getCarrier());
        return ResponseEntity.ok(artOrderService.shipOrder(id, shipmentRequest));
    }

    @PostMapping("/{id}/deliver")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Mark order as delivered (Admin/Manager)")
    public ResponseEntity<ArtOrderResponseDto> markDelivered(@PathVariable String id) {
        log.info("Marking order {} as delivered", id);
        return ResponseEntity.ok(artOrderService.markDelivered(id));
    }
}
