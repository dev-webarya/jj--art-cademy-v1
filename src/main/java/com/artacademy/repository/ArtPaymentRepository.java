package com.artacademy.repository;

import com.artacademy.entity.ArtPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtPaymentRepository extends JpaRepository<ArtPayment, UUID> {
    Optional<ArtPayment> findByOrderId(UUID orderId);

    Optional<ArtPayment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<ArtPayment> findByRazorpayPaymentId(String razorpayPaymentId);
}
