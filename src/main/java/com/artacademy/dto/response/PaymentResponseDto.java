package com.artacademy.dto.response;

import com.artacademy.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class PaymentResponseDto {
    private UUID id;
    private UUID orderId;
    private String transactionId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus status;
    private Instant processedAt;

    // For Razorpay Frontend Integration
    private String razorpayOrderId;
    private String keyId;
}