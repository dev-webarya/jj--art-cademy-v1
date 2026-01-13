package com.artacademy.dto.response;

import com.artacademy.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class ArtPaymentResponseDto {
    private UUID id;
    private UUID orderId;
    private String orderNumber;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpayKeyId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private Instant processedAt;
}
