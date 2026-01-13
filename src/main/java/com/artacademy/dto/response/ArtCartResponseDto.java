package com.artacademy.dto.response;

import com.artacademy.enums.ArtItemType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class ArtCartResponseDto {
    private UUID id;
    private UUID userId;
    private List<ArtCartItemDto> items;
    private BigDecimal totalPrice;
    private Instant updatedAt;

    @Data
    public static class ArtCartItemDto {
        private UUID id;
        private UUID itemId;
        private ArtItemType itemType;
        private String itemName;
        private String imageUrl;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}
