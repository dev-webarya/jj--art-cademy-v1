package com.artacademy.repository;

import com.artacademy.entity.ArtPayment;
import com.artacademy.enums.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtPaymentRepository extends MongoRepository<ArtPayment, String> {

    Optional<ArtPayment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<ArtPayment> findByRazorpayPaymentId(String razorpayPaymentId);

    @Query("{'order.orderId': ?0}")
    List<ArtPayment> findByOrderId(String orderId);

    @Query("{'user.userId': ?0}")
    List<ArtPayment> findByUserId(String userId);

    @Query("{'status': ?0}")
    List<ArtPayment> findByStatus(PaymentStatus status);
}
