package com.artacademy.dto.response;

import com.artacademy.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDto {
    private UUID id;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalPrice;

    private String fulfillmentType;
    private UUID fulfillmentStoreId;
    private String fulfillmentStoreName;

    private String shippingAddress;
    private String billingAddress;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private String userEmail;

    private List<OrderItemDto> items;
    private List<StatusHistoryDto> statusHistory;

    @Data
    public static class OrderItemDto {
        private UUID productId;
        private String productName;
        private String productSku;
        private Integer quantity;
        private BigDecimal priceAtPurchase;
        private BigDecimal subTotal;
    }

    @Data
    public static class StatusHistoryDto {
        private OrderStatus fromStatus;
        private OrderStatus toStatus;
        private String notes;
        private Instant changedAt;
    }
}