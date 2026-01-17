package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unified shopping cart for both ArtWorks and ArtMaterials.
 * Cart items are embedded directly in this document for atomic operations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class ArtShoppingCart {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    @Builder.Default
    private List<ArtCartItem> items = new ArrayList<>();

    @LastModifiedDate
    private Instant updatedAt;

    /**
     * Calculate total price of all items in cart
     */
    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(ArtCartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Add item to cart - handles duplicates by incrementing quantity
     */
    public void addItem(ArtCartItem item) {
        // Check if item already exists
        ArtCartItem existing = items.stream()
                .filter(i -> i.getItemId().equals(item.getItemId())
                        && i.getItemType() == item.getItemType())
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        } else {
            // Generate unique ID for new item
            item.setId(UUID.randomUUID().toString());
            items.add(item);
        }
    }

    /**
     * Remove item from cart by item ID
     */
    public boolean removeItem(String cartItemId) {
        return items.removeIf(item -> item.getId().equals(cartItemId));
    }

    /**
     * Update quantity of an item
     */
    public boolean updateItemQuantity(String cartItemId, int quantity) {
        for (ArtCartItem item : items) {
            if (item.getId().equals(cartItemId)) {
                if (quantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Clear all items from cart
     */
    public void clearItems() {
        items.clear();
    }
}
