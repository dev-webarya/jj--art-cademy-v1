package com.artacademy.service;

import com.artacademy.dto.request.ArtCartItemRequestDto;
import com.artacademy.dto.response.ArtCartResponseDto;

public interface ArtCartService {
    ArtCartResponseDto getMyCart();

    ArtCartResponseDto addToCart(ArtCartItemRequestDto request);

    ArtCartResponseDto updateQuantity(String itemId, Integer quantity);

    ArtCartResponseDto removeFromCart(String itemId);

    void clearCart();
}
