package com.artacademy.dto.response;

import com.artacademy.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ArtPaymentResponseDto {
    private String id;
    private String orderId;
    private String orderNumber;
    private String userId;
    private String userEmail;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpayKeyId;
    private String razorpaySignature;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private Instant processedAt;
    private Instant createdAt;
}
