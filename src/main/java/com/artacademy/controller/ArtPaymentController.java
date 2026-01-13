package com.artacademy.controller;

import com.artacademy.dto.request.ArtPaymentRequestDto;
import com.artacademy.dto.response.ArtPaymentResponseDto;
import com.artacademy.service.ArtPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/art-payments")
@RequiredArgsConstructor
@Slf4j
public class ArtPaymentController {

    private final ArtPaymentService artPaymentService;

    @PostMapping("/initiate")
    public ResponseEntity<ArtPaymentResponseDto> initiatePayment(@Valid @RequestBody ArtPaymentRequestDto request) {
        log.info("Initiating payment for order: {}", request.getOrderId());
        return ResponseEntity.ok(artPaymentService.initiatePayment(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<ArtPaymentResponseDto> verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature) {
        log.info("Verifying payment for Razorpay order: {}", razorpayOrderId);
        return ResponseEntity
                .ok(artPaymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ArtPaymentResponseDto> getPaymentByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(artPaymentService.getPaymentByOrderId(orderId));
    }
}
