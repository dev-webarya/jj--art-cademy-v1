package com.artacademy.service;

import com.artacademy.dto.request.CartCheckoutRequestDto;
import com.artacademy.dto.request.OrderRequestDto;
import com.artacademy.dto.response.OrderResponseDto;
import com.artacademy.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderService {

    // Standard single/list buy
    OrderResponseDto createOrder(OrderRequestDto request);

    // NEW: Buy everything in cart
    OrderResponseDto checkoutCart(CartCheckoutRequestDto request);

    // NEW: Confirm payment (System bypass for customers)
    OrderResponseDto confirmOrderPayment(UUID orderId, String paymentMethod);

    Page<OrderResponseDto> getAllOrders(
            OrderStatus status,
            String orderNumber,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime startDate,
            LocalDateTime endDate,
            UUID userIdFilter,
            Pageable pageable);

    OrderResponseDto getOrderById(UUID id);

    OrderResponseDto updateOrderStatus(UUID orderId, OrderStatus newStatus, String notes);
}