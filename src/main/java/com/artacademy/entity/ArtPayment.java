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
@Document(collection = "art_payments")
public class ArtPayment {

    @Id
    private String id;

    // Order reference
    private OrderRef order;

    // User reference
    private UserRef user;

    @Indexed(unique = true, sparse = true)
    private String razorpayOrderId;

    @Indexed(unique = true, sparse = true)
    private String razorpayPaymentId;

    private String razorpaySignature;

    private BigDecimal amount;

    @Builder.Default
    private String currency = "INR";

    @Indexed
    private PaymentStatus status;

    @CreatedDate
    private Instant processedAt;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderRef {
        private String orderId;
        private String orderNumber;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRef {
        private String userId;
        private String email;
    }
}
