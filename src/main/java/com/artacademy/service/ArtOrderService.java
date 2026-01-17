package com.artacademy.service;

import com.artacademy.dto.request.ArtCartCheckoutRequestDto;
import com.artacademy.dto.request.ArtOrderRequestDto;
import com.artacademy.dto.response.ArtOrderResponseDto;
import com.artacademy.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ArtOrderService {
    ArtOrderResponseDto createOrder(ArtOrderRequestDto request);

    ArtOrderResponseDto checkoutCart(ArtCartCheckoutRequestDto request);

    ArtOrderResponseDto getById(String id);

    Page<ArtOrderResponseDto> getAll(OrderStatus status, String orderNumber, BigDecimal minPrice,
            BigDecimal maxPrice, LocalDateTime startDate, LocalDateTime endDate, String userId, Pageable pageable);

    Page<ArtOrderResponseDto> getMyOrders(Pageable pageable);

    ArtOrderResponseDto updateStatus(String id, OrderStatus status, String notes);

    /**
     * Rollback order - used when payment fails.
     * Restores stock and marks order as cancelled.
     */
    void rollbackOrder(String orderId, String reason);
}
