package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtPaymentRequestDto;
import com.artacademy.dto.response.ArtPaymentResponseDto;
import com.artacademy.entity.ArtOrder;
import com.artacademy.entity.ArtPayment;
import com.artacademy.entity.User;
import com.artacademy.enums.OrderStatus;
import com.artacademy.enums.PaymentStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtPaymentMapper;
import com.artacademy.repository.ArtOrderRepository;
import com.artacademy.repository.ArtPaymentRepository;
import com.artacademy.repository.UserRepository;
import com.artacademy.service.ArtOrderService;
import com.artacademy.service.ArtPaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Payment service implementation for MongoDB with Razorpay integration.
 * Implements compensating transactions for rollback on payment failure.
 */
@Service
@Slf4j
public class ArtPaymentServiceImpl implements ArtPaymentService {

    private final ArtPaymentRepository paymentRepository;
    private final ArtOrderRepository orderRepository;
    private final ArtOrderService orderService;
    private final ArtPaymentMapper paymentMapper;
    private final UserRepository userRepository;

    @Value("${razorpay.key.id:rzp_test_placeholder}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret:secret_placeholder}")
    private String razorpayKeySecret;

    public ArtPaymentServiceImpl(ArtPaymentRepository paymentRepository,
            ArtOrderRepository orderRepository,
            ArtOrderService orderService,
            ArtPaymentMapper paymentMapper,
            UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.paymentMapper = paymentMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ArtPaymentResponseDto initiatePayment(ArtPaymentRequestDto request) {
        log.info("Initiating payment for order: {}", request.getOrderId());

        ArtOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", request.getOrderId()));

        // Security check: verify user owns this order or is admin/manager
        verifyOrderOwnership(order);

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
                    .orderId(order.getId())
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
    public ArtPaymentResponseDto verifyPayment(String razorpayOrderId, String razorpayPaymentId,
            String razorpaySignature) {
        log.info("Verifying payment for Razorpay order: {}", razorpayOrderId);

        ArtPayment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtPayment", "razorpayOrderId", razorpayOrderId));

        ArtOrder order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", payment.getOrderId()));

        try {
            // Verify signature
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            boolean isValid = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);

            if (isValid) {
                // Payment successful
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus(PaymentStatus.SUCCESS);

                // Update order status to PROCESSING (this also deducts stock)
                order.setStatus(OrderStatus.PROCESSING);
                order.addStatusHistory(OrderStatus.PROCESSING, "Payment verified successfully");
                orderRepository.save(order);

                log.info("Payment verified successfully for order: {}", order.getOrderNumber());
            } else {
                // Payment verification failed - trigger rollback
                handlePaymentFailure(payment, order, "Payment signature verification failed");
            }

            return paymentMapper.toDto(paymentRepository.save(payment));

        } catch (RazorpayException e) {
            log.error("Razorpay verification error: {}", e.getMessage());
            // Payment failed - trigger rollback
            handlePaymentFailure(payment, order, "Razorpay verification error: " + e.getMessage());
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }

    /**
     * Handle payment failure - marks payment as failed and triggers order rollback.
     */
    private void handlePaymentFailure(ArtPayment payment, ArtOrder order, String errorMessage) {
        log.warn("Payment failed for order: {}, reason: {}", order.getOrderNumber(), errorMessage);

        payment.setStatus(PaymentStatus.FAILED);
        payment.setErrorMessage(errorMessage);
        paymentRepository.save(payment);

        // Trigger order rollback (cancels order and restores stock if applicable)
        orderService.rollbackOrder(order.getId(), "Payment failed: " + errorMessage);
    }

    @Override
    public ArtPaymentResponseDto getPaymentByOrderId(String orderId) {
        ArtOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", orderId));

        // Security check: verify user owns this order or is admin/manager
        verifyOrderOwnership(order);

        ArtPayment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtPayment", "orderId", orderId));
        ArtPaymentResponseDto response = paymentMapper.toDto(payment);
        response.setRazorpayKeyId(razorpayKeyId);
        return response;
    }

    private void verifyOrderOwnership(ArtOrder order) {
        User currentUser = getCurrentUser();
        boolean isAdminOrManager = currentUser.getRoles().contains("ROLE_ADMIN")
                || currentUser.getRoles().contains("ROLE_MANAGER");

        if (!isAdminOrManager && !order.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this order's payment");
        }
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
