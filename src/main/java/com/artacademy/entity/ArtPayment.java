package com.artacademy.entity;

import com.artacademy.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class ArtPayment {

    @Id
    private String id;

    @Indexed
    private String orderId;

    @Indexed(unique = true, sparse = true)
    private String razorpayOrderId;

    @Indexed(unique = true, sparse = true)
    private String razorpayPaymentId;

    private String razorpaySignature;

    private BigDecimal amount;

    @Builder.Default
    private String currency = "INR";

    private PaymentStatus status;

    private String errorMessage; // Store error details on failure

    @CreatedDate
    private Instant processedAt;
}
