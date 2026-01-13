package com.artacademy.service.impl;

import com.artacademy.dto.request.CartItemRequestDto;
import com.artacademy.dto.response.CartResponseDto;
import com.artacademy.entity.CartItem;
import com.artacademy.entity.Product;
import com.artacademy.entity.ShoppingCart;
import com.artacademy.entity.User;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.EngagementMapper;
import com.artacademy.repository.CartRepository;
import com.artacademy.repository.ProductRepository;
import com.artacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EngagementMapper engagementMapper;

    @Transactional
    public CartResponseDto addToCart(CartItemRequestDto request) {
        log.info("Adding product {} to cart, quantity: {}", request.getProductId(), request.getQuantity());
        User user = getCurrentUser();
        ShoppingCart cart = getOrCreateCart(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Check if item exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(newItem);
        }

        return engagementMapper.toCartDto(cartRepository.save(cart));
    }

    @Transactional
    public CartResponseDto removeFromCart(UUID productId) {
        User user = getCurrentUser();
        ShoppingCart cart = getOrCreateCart(user);

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return engagementMapper.toCartDto(cartRepository.save(cart));
    }

    @Transactional(readOnly = true)
    public CartResponseDto getMyCart() {
        return engagementMapper.toCartDto(getOrCreateCart(getCurrentUser()));
    }

    @Transactional
    public void clearCart() {
        log.warn("Clearing cart for current user");
        ShoppingCart cart = getOrCreateCart(getCurrentUser());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private ShoppingCart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(ShoppingCart.builder().user(user).build()));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}