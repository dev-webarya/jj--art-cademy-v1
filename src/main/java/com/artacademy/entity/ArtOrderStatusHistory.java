package com.artacademy.entity;

import com.artacademy.enums.OrderStatus;
import lombok.*;

import java.time.Instant;

/**
 * Embedded document for order status history - not a separate collection.
 * Stored directly inside ArtOrder document.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtOrderStatusHistory {

    private OrderStatus status;

    private String notes;

    @Builder.Default
    private Instant changedAt = Instant.now();
}
