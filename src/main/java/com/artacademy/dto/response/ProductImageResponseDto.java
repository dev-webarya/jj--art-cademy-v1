package com.artacademy.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class ProductImageResponseDto {
    private UUID id;
    private UUID productId;
    private String imageUrl;
    private boolean isPrimary;
    private Instant createdAt;
    private Instant updatedAt;
}
