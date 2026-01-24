package com.artacademy.service;

import com.artacademy.dto.request.ArtPaymentRequestDto;
import com.artacademy.dto.response.ArtPaymentResponseDto;

public interface ArtPaymentService {

    /**
     * Initiate payment for an order. Creates Razorpay order.
     */
    ArtPaymentResponseDto initiatePayment(ArtPaymentRequestDto request);

    /**
     * Verify payment after Razorpay callback.
     * On success: updates order status to PROCESSING and deducts stock.
     * On failure: triggers order rollback and restores stock.
     */
    ArtPaymentResponseDto verifyPayment(String razorpayOrderId, String razorpayPaymentId,
            String razorpaySignature);

    /**
     * Get payment details by order ID.
     */
    ArtPaymentResponseDto getPaymentByOrderId(String orderId);
}
