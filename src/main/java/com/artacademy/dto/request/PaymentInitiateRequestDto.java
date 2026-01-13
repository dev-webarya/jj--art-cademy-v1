package com.artacademy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentInitiateRequestDto {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    // Amount is optional - if not provided, it will be auto-populated from the
    // order's totalPrice
    // This is a security measure to prevent clients from paying less than the
    // actual order amount
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required (e.g., RAZORPAY, CARD)")
    private String paymentMethod;
}