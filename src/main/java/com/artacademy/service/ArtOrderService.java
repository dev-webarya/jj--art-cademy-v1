package com.artacademy.service;

import com.artacademy.dto.request.ArtCartCheckoutRequestDto;
import com.artacademy.dto.request.ArtOrderRequestDto;
import com.artacademy.dto.response.ArtOrderResponseDto;
import com.artacademy.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ArtOrderService {
    ArtOrderResponseDto createOrder(ArtOrderRequestDto request);

    ArtOrderResponseDto checkoutCart(ArtCartCheckoutRequestDto request);

    ArtOrderResponseDto getById(UUID id);

    Page<ArtOrderResponseDto> getAll(OrderStatus status, String orderNumber, BigDecimal minPrice,
            BigDecimal maxPrice, LocalDateTime startDate, LocalDateTime endDate, UUID userId, Pageable pageable);

    Page<ArtOrderResponseDto> getMyOrders(Pageable pageable);

    ArtOrderResponseDto updateStatus(UUID id, OrderStatus status, String notes);
}
