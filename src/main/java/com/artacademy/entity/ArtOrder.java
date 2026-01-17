package com.artacademy.entity;

import com.artacademy.enums.OrderStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unified order for both ArtWorks and ArtMaterials.
 * Order items and status history are embedded directly for atomic operations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
@CompoundIndex(name = "user_date_idx", def = "{'userId': 1, 'createdAt': -1}")
public class ArtOrder {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderNumber;

    @Indexed
    private String userId;

    private OrderStatus status;

    private BigDecimal totalPrice;

    private String shippingAddress;

    private String billingAddress;

    @Builder.Default
    private List<ArtOrderItem> items = new ArrayList<>();

    @Builder.Default
    private List<ArtOrderStatusHistory> statusHistory = new ArrayList<>();

    // Shipment tracking fields
    private String trackingNumber;
    private String carrier; // e.g., "FedEx", "DHL", "BlueDart"
    private String trackingUrl;
    private Instant shippedAt;
    private Instant estimatedDelivery;
    private Instant deliveredAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    /**
     * Add status history entry
     */
    public void addStatusHistory(OrderStatus status, String notes) {
        ArtOrderStatusHistory history = ArtOrderStatusHistory.builder()
                .status(status)
                .notes(notes)
                .changedAt(Instant.now())
                .build();
        this.statusHistory.add(history);
    }

    /**
     * Convert cart items to order items
     */
    public void addItemsFromCart(List<ArtCartItem> cartItems) {
        for (ArtCartItem cartItem : cartItems) {
            ArtOrderItem orderItem = ArtOrderItem.builder()
                    .id(UUID.randomUUID().toString())
                    .itemId(cartItem.getItemId())
                    .itemType(cartItem.getItemType())
                    .itemName(cartItem.getItemName())
                    .imageUrl(cartItem.getImageUrl())
                    .unitPrice(cartItem.getUnitPrice())
                    .quantity(cartItem.getQuantity())
                    .itemVariantId(cartItem.getItemVariantId())
                    .itemVariantName(cartItem.getItemVariantName())
                    .build();
            items.add(orderItem);
        }
    }

    /**
     * Calculate total price from items
     */
    public void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .map(ArtOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
