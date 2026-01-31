package com.artacademy.repository;

import com.artacademy.entity.LmsStudentSubscription;
import com.artacademy.enums.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LmsStudentSubscriptionRepository extends MongoRepository<LmsStudentSubscription, String> {

    List<LmsStudentSubscription> findByStudentId(String studentId);

    Optional<LmsStudentSubscription> findByStudentIdAndStatus(String studentId, SubscriptionStatus status);

    Optional<LmsStudentSubscription> findByStudentIdAndSubscriptionMonthAndSubscriptionYear(
            String studentId, Integer month, Integer year);

    Page<LmsStudentSubscription> findByStatus(SubscriptionStatus status, Pageable pageable);

    List<LmsStudentSubscription> findBySubscriptionMonthAndSubscriptionYear(Integer month, Integer year);

    List<LmsStudentSubscription> findByStatusAndAttendedSessionsGreaterThan(
            SubscriptionStatus status, Integer allowedSessions);

    boolean existsByStudentIdAndSubscriptionMonthAndSubscriptionYear(
            String studentId, Integer month, Integer year);
}
