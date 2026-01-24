package com.artacademy.dto.response;

import com.artacademy.enums.ArtItemType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class ArtCartResponseDto {
    private String id;
    private String userId;
    private List<ArtCartItemDto> items;
    private BigDecimal totalPrice;
    private Instant updatedAt;

    @Data
    public static class ArtCartItemDto {
        private String id;
        private String itemId;
        private ArtItemType itemType;
        private String itemName;
        private String imageUrl;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}
