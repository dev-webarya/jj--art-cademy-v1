package com.artacademy.controller;

import com.artacademy.dto.request.ArtCartItemRequestDto;
import com.artacademy.dto.response.ArtCartResponseDto;
import com.artacademy.service.ArtCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/art-cart")
@RequiredArgsConstructor
@Slf4j
public class ArtCartController {

    private final ArtCartService artCartService;

    @GetMapping
    public ResponseEntity<ArtCartResponseDto> getMyCart() {
        log.debug("Fetching cart for current user");
        return ResponseEntity.ok(artCartService.getMyCart());
    }

    @PostMapping("/items")
    public ResponseEntity<ArtCartResponseDto> addItem(@Valid @RequestBody ArtCartItemRequestDto request) {
        log.info("Adding item {} (type: {}) to cart", request.getItemId(), request.getItemType());
        return ResponseEntity.ok(artCartService.addToCart(request));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ArtCartResponseDto> updateQuantity(@PathVariable UUID itemId,
            @RequestParam Integer quantity) {
        log.info("Updating cart item {} quantity to: {}", itemId, quantity);
        return ResponseEntity.ok(artCartService.updateQuantity(itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ArtCartResponseDto> removeItem(@PathVariable UUID itemId) {
        log.info("Removing cart item: {}", itemId);
        return ResponseEntity.ok(artCartService.removeFromCart(itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        log.warn("Clearing cart for current user");
        artCartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
