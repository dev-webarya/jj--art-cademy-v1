package com.artacademy.dto.response;

import com.artacademy.enums.ArtItemType;
import com.artacademy.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class ArtOrderResponseDto {
    private String id;
    private String orderNumber;
    private String userId;
    private String userEmail;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String shippingAddress;
    private String billingAddress;
    private List<ArtOrderItemDto> items;
    private List<ArtOrderStatusHistoryDto> statusHistory;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    public static class ArtOrderItemDto {
        private String itemId;
        private ArtItemType itemType;
        private String itemName;
        private String imageUrl;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }

    @Data
    public static class ArtOrderStatusHistoryDto {
        private OrderStatus status;
        private String notes;
        private Instant changedAt;
    }
}
