package com.artacademy.service;

import com.artacademy.dto.request.ArtPaymentRequestDto;
import com.artacademy.dto.response.ArtPaymentResponseDto;

import java.util.UUID;

public interface ArtPaymentService {
    ArtPaymentResponseDto initiatePayment(ArtPaymentRequestDto request);

    ArtPaymentResponseDto verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);

    ArtPaymentResponseDto getPaymentByOrderId(UUID orderId);
}
