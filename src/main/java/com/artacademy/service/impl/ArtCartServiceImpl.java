package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtCartItemRequestDto;
import com.artacademy.dto.response.ArtCartResponseDto;
import com.artacademy.entity.*;
import com.artacademy.enums.ArtItemType;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtCartMapper;
import com.artacademy.repository.ArtCartRepository;
import com.artacademy.repository.ArtMaterialsRepository;
import com.artacademy.repository.ArtWorksRepository;
import com.artacademy.repository.UserRepository;
import com.artacademy.service.ArtCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtCartServiceImpl implements ArtCartService {

    private final ArtCartRepository cartRepository;
    private final ArtWorksRepository artWorksRepository;
    private final ArtMaterialsRepository artMaterialsRepository;
    private final UserRepository userRepository;
    private final ArtCartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public ArtCartResponseDto getMyCart() {
        return cartMapper.toDto(getOrCreateCart(getCurrentUser()));
    }

    @Override
    @Transactional
    public ArtCartResponseDto addToCart(ArtCartItemRequestDto request) {
        log.info("Adding item {} (type: {}) to cart", request.getItemId(), request.getItemType());
        User user = getCurrentUser();
        ArtShoppingCart cart = getOrCreateCart(user);

        // Get item details based on type
        String itemName;
        String imageUrl;
        BigDecimal unitPrice;

        if (request.getItemType() == ArtItemType.ARTWORK) {
            ArtWorks artwork = artWorksRepository.findById(request.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", request.getItemId()));
            itemName = artwork.getName();
            imageUrl = artwork.getImageUrl();
            unitPrice = artwork.getBasePrice();
        } else {
            ArtMaterials material = artMaterialsRepository.findById(request.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", request.getItemId()));
            itemName = material.getName();
            imageUrl = material.getImageUrl();
            // Apply discount if any
            BigDecimal discount = BigDecimal.valueOf(material.getDiscount()).divide(BigDecimal.valueOf(100));
            unitPrice = material.getBasePrice().subtract(material.getBasePrice().multiply(discount));
        }

        // Check if item already in cart
        Optional<ArtCartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getItemId().equals(request.getItemId())
                        && item.getItemType() == request.getItemType())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            ArtCartItem newItem = ArtCartItem.builder()
                    .cart(cart)
                    .itemId(request.getItemId())
                    .itemType(request.getItemType())
                    .itemName(itemName)
                    .imageUrl(imageUrl)
                    .unitPrice(unitPrice)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(newItem);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public ArtCartResponseDto updateQuantity(UUID itemId, Integer quantity) {
        User user = getCurrentUser();
        ArtShoppingCart cart = getOrCreateCart(user);

        ArtCartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("ArtCartItem", "id", itemId));

        if (quantity <= 0) {
            cart.getItems().remove(cartItem);
        } else {
            cartItem.setQuantity(quantity);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public ArtCartResponseDto removeFromCart(UUID itemId) {
        User user = getCurrentUser();
        ArtShoppingCart cart = getOrCreateCart(user);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearCart() {
        log.warn("Clearing cart for current user");
        ArtShoppingCart cart = getOrCreateCart(getCurrentUser());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private ArtShoppingCart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(ArtShoppingCart.builder().user(user).build()));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
