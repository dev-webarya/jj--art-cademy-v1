package com.artacademy.controller;

import com.artacademy.dto.request.ArtCartCheckoutRequestDto;
import com.artacademy.dto.request.ArtOrderRequestDto;
import com.artacademy.dto.response.ArtOrderResponseDto;
import com.artacademy.enums.OrderStatus;
import com.artacademy.service.ArtOrderService;
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
public class ArtOrderController {

    private final ArtOrderService artOrderService;

    @PostMapping
    public ResponseEntity<ArtOrderResponseDto> createOrder(@Valid @RequestBody ArtOrderRequestDto request) {
        log.info("Creating order with {} items", request.getItems().size());
        return new ResponseEntity<>(artOrderService.createOrder(request), HttpStatus.CREATED);
    }

    @PostMapping("/checkout")
    public ResponseEntity<ArtOrderResponseDto> checkoutCart(@Valid @RequestBody ArtCartCheckoutRequestDto request) {
        log.info("Checking out cart");
        return new ResponseEntity<>(artOrderService.checkoutCart(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtOrderResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artOrderService.getById(id));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<Page<ArtOrderResponseDto>> getMyOrders(Pageable pageable) {
        return ResponseEntity.ok(artOrderService.getMyOrders(pageable));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Page<ArtOrderResponseDto>> getAll(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String orderNumber,
            Pageable pageable) {
        return ResponseEntity.ok(artOrderService.getAll(status, orderNumber, null, null, null, null, null, pageable));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ArtOrderResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String notes) {
        log.info("Updating order {} status to: {}", id, status);
        return ResponseEntity.ok(artOrderService.updateStatus(id, status, notes));
    }
}
