package com.artacademy.entity;

import com.artacademy.enums.ArtItemType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "art_shopping_carts")
public class ArtShoppingCart {

    @Id
    private String id;

    // User reference - one cart per user
    @Indexed(unique = true)
    private String userId;

    private String userEmail;

    // Embedded cart items
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @LastModifiedDate
    private Instant updatedAt;

    /**
     * Calculate total price of all items in cart
     */
    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Embedded class for cart items
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private String itemId;
        private ArtItemType itemType;
        private String itemName;
        private String imageUrl;
        private BigDecimal unitPrice;
        private Integer quantity;

        public BigDecimal getSubtotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
