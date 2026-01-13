package com.artacademy.controller;

import com.artacademy.dto.request.CartCheckoutRequestDto;
import com.artacademy.dto.request.OrderRequestDto;
import com.artacademy.dto.response.OrderResponseDto;
import com.artacademy.enums.OrderStatus;
import com.artacademy.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // 1. Buy Now (Specific items provided in request)
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request) {
        log.info("Creating order with {} items", request.getItems().size());
        OrderResponseDto order = orderService.createOrder(request);
        log.info("Order created with number: {}", order.getOrderNumber());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // 2. Checkout Cart (Buy everything currently in the user's cart)
    @PostMapping("/checkout-cart")
    public ResponseEntity<OrderResponseDto> checkoutCart(@Valid @RequestBody CartCheckoutRequestDto request) {
        log.info("Checking out cart for customer");
        OrderResponseDto order = orderService.checkoutCart(request);
        log.info("Cart checkout complete, order number: {}", order.getOrderNumber());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // 3. Get All Orders (Dynamic Search & Filter)
    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) UUID userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderResponseDto> orders = orderService.getAllOrders(
                status, orderNumber, minPrice, maxPrice, startDate, endDate, userId, pageable);
        return ResponseEntity.ok(orders);
    }

    // 4. Get Order By ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable UUID id) {
        log.debug("Fetching order by ID: {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // 5. Update Status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<OrderResponseDto> updateStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String notes) {
        log.info("Updating order {} status to: {}", id, status);
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status, notes));
    }
}