package com.artacademy.repository;

import com.artacademy.entity.ArtPayment;
import com.artacademy.enums.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtPaymentRepository extends MongoRepository<ArtPayment, String> {

    Optional<ArtPayment> findByOrderId(String orderId);

    Optional<ArtPayment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<ArtPayment> findByRazorpayPaymentId(String razorpayPaymentId);

    List<ArtPayment> findByStatus(PaymentStatus status);
}
