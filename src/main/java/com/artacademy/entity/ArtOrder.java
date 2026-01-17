package com.artacademy.entity;

import com.artacademy.enums.OrderStatus;
import com.artacademy.enums.ArtItemType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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
@Document(collection = "art_orders")
public class ArtOrder {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderNumber;

    // Embedded user reference
    private UserRef user;

    private OrderStatus status;

    private BigDecimal totalPrice;

    private String shippingAddress;

    private String billingAddress;

    // Embedded order items
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Embedded status history
    @Builder.Default
    private List<StatusHistory> statusHistory = new ArrayList<>();

    @Builder.Default
    private boolean deleted = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    /**
     * Add status history entry
     */
    public void addStatusHistory(OrderStatus status, String notes) {
        StatusHistory history = StatusHistory.builder()
                .status(status)
                .notes(notes)
                .changedAt(Instant.now())
                .build();
        this.statusHistory.add(history);
    }

    // Embedded class for user reference
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRef {
        private String userId;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
    }

    // Embedded class for order items
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
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

    // Embedded class for status history
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusHistory {
        private OrderStatus status;
        private String notes;
        private Instant changedAt;
    }
}
