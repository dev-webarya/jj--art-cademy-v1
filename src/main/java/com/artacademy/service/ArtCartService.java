package com.artacademy.service;

import com.artacademy.dto.request.ArtCartItemRequestDto;
import com.artacademy.dto.response.ArtCartResponseDto;

import java.util.UUID;

public interface ArtCartService {
    ArtCartResponseDto getMyCart();

    ArtCartResponseDto addToCart(ArtCartItemRequestDto request);

    ArtCartResponseDto updateQuantity(UUID itemId, Integer quantity);

    ArtCartResponseDto removeFromCart(UUID itemId);

    void clearCart();
}
