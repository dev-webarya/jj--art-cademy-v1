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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Cart service implementation for MongoDB.
 * Uses embedded cart items for atomic operations.
 */
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
    public ArtCartResponseDto getMyCart() {
        return cartMapper.toDto(getOrCreateCart(getCurrentUser()));
    }

    @Override
    public ArtCartResponseDto addToCart(ArtCartItemRequestDto request) {
        log.info("Adding item {} (type: {}) to cart", request.getItemId(), request.getItemType());
        User user = getCurrentUser();
        ArtShoppingCart cart = getOrCreateCart(user);

        // Get item details based on type
        String itemName;
        String imageUrl;
        BigDecimal unitPrice;
        String variantId = null;
        String variantName = null;

        if (request.getItemType() == ArtItemType.ARTWORK) {
            ArtWorks artwork = artWorksRepository.findByIdAndDeletedFalse(request.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", request.getItemId()));
            itemName = artwork.getName();
            imageUrl = artwork.getImageUrl();
            // Use discount price if available
            unitPrice = (artwork.getDiscountPrice() != null
                    && artwork.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0)
                            ? artwork.getDiscountPrice()
                            : artwork.getBasePrice();
        } else {
            ArtMaterials material = artMaterialsRepository.findByIdAndDeletedFalse(request.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", request.getItemId()));
            itemName = material.getName();
            imageUrl = material.getImageUrl();

            if (request.getItemVariantId() != null) {
                // Find specific variant
                ArtMaterials.MaterialVariant variant = material.getVariants().stream()
                        .filter(v -> v.getId().equals(request.getItemVariantId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("MaterialVariant", "id",
                                request.getItemVariantId()));

                variantId = variant.getId();
                variantName = variant.getSize();
                itemName = itemName + " (" + variant.getSize() + ")";

                // Use variant discount price if available
                unitPrice = (variant.getDiscountPrice() != null
                        && variant.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0)
                                ? variant.getDiscountPrice()
                                : variant.getPrice();
            } else {
                // Fallback to legacy logic
                if (material.getDiscount() != null && material.getDiscount() > 0) {
                    BigDecimal discount = BigDecimal.valueOf(material.getDiscount())
                            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                    unitPrice = material.getBasePrice().subtract(material.getBasePrice().multiply(discount));
                } else {
                    unitPrice = material.getBasePrice();
                }
            }
        }

        // Use the cart's addItem method which handles duplicates
        // Note: Logic in cart.addItem might need update to differentiating variants of
        // same item?
        // Current logic probably checks itemId only. We need to check itemId +
        // variantId.
        // For now, construct the item.
        ArtCartItem newItem = ArtCartItem.builder()
                .itemId(request.getItemId())
                .itemType(request.getItemType())
                .itemName(itemName)
                .imageUrl(imageUrl)
                .unitPrice(unitPrice)
                .quantity(request.getQuantity())
                .itemVariantId(variantId)
                .itemVariantName(variantName)
                .build();

        // IMPORTANT: We need to ensure cart.addItem handles variants!
        // If ArtShoppingCart.addItem compares only itemId, we might overwrite different
        // variants.
        // We should check ArtShoppingCart.java next.
        cart.addItem(newItem);

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public ArtCartResponseDto updateQuantity(String itemId, Integer quantity) {
        User user = getCurrentUser();
        ArtShoppingCart cart = getOrCreateCart(user);

        boolean updated = cart.updateItemQuantity(itemId, quantity);
        if (!updated) {
            throw new ResourceNotFoundException("ArtCartItem", "id", itemId);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public ArtCartResponseDto removeFromCart(String itemId) {
        User user = getCurrentUser();
        ArtShoppingCart cart = getOrCreateCart(user);
        cart.removeItem(itemId);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public void clearCart() {
        log.warn("Clearing cart for current user");
        ArtShoppingCart cart = getOrCreateCart(getCurrentUser());
        cart.clearItems();
        cartRepository.save(cart);
    }

    private ArtShoppingCart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(
                        ArtShoppingCart.builder().userId(user.getId()).build()));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
