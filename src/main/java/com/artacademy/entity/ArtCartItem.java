package com.artacademy.entity;

import com.artacademy.enums.ArtItemType;
import lombok.*;

import java.math.BigDecimal;

/**
 * Embedded document for cart items - not a separate collection.
 * Stored directly inside ArtShoppingCart document.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtCartItem {

    private String id; // Generate unique ID manually

    private String itemId;

    private ArtItemType itemType;

    private String itemName;

    private String imageUrl;

    private String itemVariantId;
    private String itemVariantName;

    private BigDecimal unitPrice;

    private Integer quantity;

    /**
     * Calculate subtotal for this cart item
     */
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
