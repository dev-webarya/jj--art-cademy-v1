package com.artacademy.controller;

import com.artacademy.dto.request.PaymentInitiateRequestDto;
import com.artacademy.dto.response.PaymentResponseDto;
import com.artacademy.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDto> initiatePayment(@Valid @RequestBody PaymentInitiateRequestDto request) {
        log.info("Initiating payment for order: {}", request.getOrderId());
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    // This endpoint mimics what Razorpay Webhook would call
    @PostMapping("/webhook/verify")
    public ResponseEntity<PaymentResponseDto> verifyPayment(@RequestParam String transactionId,
            @RequestParam String status) {
        log.info("Verifying payment for transaction: {} with status: {}", transactionId, status);
        return ResponseEntity.ok(paymentService.verifyPayment(transactionId, status));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}