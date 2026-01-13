package com.artacademy.controller;

import com.artacademy.dto.request.CartItemRequestDto;
import com.artacademy.dto.response.CartResponseDto;
import com.artacademy.service.impl.CartServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> getMyCart() {
        log.debug("Fetching cart for current user");
        return ResponseEntity.ok(cartService.getMyCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addItem(@Valid @RequestBody CartItemRequestDto request) {
        log.info("Adding product {} to cart, quantity: {}", request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDto> removeItem(@PathVariable UUID productId) {
        log.info("Removing product {} from cart", productId);
        return ResponseEntity.ok(cartService.removeFromCart(productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        log.warn("Clearing entire cart for current user");
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}