package com.artacademy.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class StockItemResponseDto {
    private UUID id;
    private Integer quantity;
    private Instant createdAt;
    private Instant updatedAt;

    // --- Enriched Data ---
    private UUID productId;
    private String productName;
    private String productSku;

    private UUID storeId; // Null for central warehouse
    private String storeName; // Null for central warehouse
}