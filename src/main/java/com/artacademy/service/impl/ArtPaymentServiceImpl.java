package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtPaymentRequestDto;
import com.artacademy.dto.response.ArtPaymentResponseDto;
import com.artacademy.entity.ArtOrder;
import com.artacademy.entity.ArtPayment;
import com.artacademy.enums.OrderStatus;
import com.artacademy.enums.PaymentStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtPaymentMapper;
import com.artacademy.repository.ArtOrderRepository;
import com.artacademy.repository.ArtPaymentRepository;
import com.artacademy.service.ArtPaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class ArtPaymentServiceImpl implements ArtPaymentService {

    private final ArtPaymentRepository paymentRepository;
    private final ArtOrderRepository orderRepository;
    private final ArtPaymentMapper paymentMapper;

    @Value("${razorpay.key.id:rzp_test_placeholder}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret:secret_placeholder}")
    private String razorpayKeySecret;

    public ArtPaymentServiceImpl(ArtPaymentRepository paymentRepository,
            ArtOrderRepository orderRepository,
            ArtPaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    @Transactional
    public ArtPaymentResponseDto initiatePayment(ArtPaymentRequestDto request) {
        log.info("Initiating payment for order: {}", request.getOrderId());

        ArtOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", request.getOrderId()));

        // Check if payment already exists for this order
        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new IllegalStateException("Payment already initiated for this order");
        }

        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Create Razorpay Order
            JSONObject orderRequest = new JSONObject();
            // Razorpay expects amount in paise (smallest currency unit)
            orderRequest.put("amount", order.getTotalPrice().multiply(BigDecimal.valueOf(100)).intValue());
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", order.getOrderNumber());

            Order razorpayOrder = razorpay.orders.create(orderRequest);

            // Create payment record
            ArtPayment payment = ArtPayment.builder()
                    .order(order)
                    .razorpayOrderId(razorpayOrder.get("id"))
                    .amount(order.getTotalPrice())
                    .currency("INR")
                    .status(PaymentStatus.PENDING)
                    .build();

            ArtPayment savedPayment = paymentRepository.save(payment);

            // Build response with Razorpay details for frontend
            ArtPaymentResponseDto response = paymentMapper.toDto(savedPayment);
            response.setRazorpayKeyId(razorpayKeyId);

            log.info("Payment initiated with Razorpay order ID: {}", (String) razorpayOrder.get("id"));
            return response;

        } catch (RazorpayException e) {
            log.error("Razorpay error: {}", e.getMessage());
            throw new RuntimeException("Failed to initiate payment: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ArtPaymentResponseDto verifyPayment(String razorpayOrderId, String razorpayPaymentId,
            String razorpaySignature) {
        log.info("Verifying payment for Razorpay order: {}", razorpayOrderId);

        ArtPayment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtPayment", "razorpayOrderId", razorpayOrderId));

        try {
            // Verify signature
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            boolean isValid = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);

            if (isValid) {
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus(PaymentStatus.SUCCESS);

                // Update order status to PROCESSING
                ArtOrder order = payment.getOrder();
                order.setStatus(OrderStatus.PROCESSING);
                order.addStatusHistory(OrderStatus.PROCESSING, "Payment verified successfully");
                orderRepository.save(order);

                log.info("Payment verified successfully for order: {}", order.getOrderNumber());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                log.warn("Payment verification failed for order: {}", payment.getOrder().getOrderNumber());
            }

            return paymentMapper.toDto(paymentRepository.save(payment));

        } catch (RazorpayException e) {
            log.error("Razorpay verification error: {}", e.getMessage());
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ArtPaymentResponseDto getPaymentByOrderId(UUID orderId) {
        ArtPayment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtPayment", "orderId", orderId));
        ArtPaymentResponseDto response = paymentMapper.toDto(payment);
        response.setRazorpayKeyId(razorpayKeyId);
        return response;
    }
}
