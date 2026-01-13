package com.artacademy.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class ReviewResponseDto {
    private UUID id;
    private String userName; // "Alice C."
    private Integer rating;
    private String title;
    private String comment;
    private boolean isVerifiedPurchase;
    private Instant createdAt;
}