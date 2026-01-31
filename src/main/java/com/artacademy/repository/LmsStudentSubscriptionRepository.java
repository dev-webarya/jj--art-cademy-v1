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

        // By studentId
        List<LmsStudentSubscription> findByStudentId(String studentId);

        Optional<LmsStudentSubscription> findByStudentIdAndStatus(String studentId, SubscriptionStatus status);

        // By enrollmentId
        List<LmsStudentSubscription> findByEnrollmentId(String enrollmentId);

        Optional<LmsStudentSubscription> findByEnrollmentIdAndSubscriptionMonthAndSubscriptionYear(
                        String enrollmentId, Integer month, Integer year);

        boolean existsByEnrollmentIdAndSubscriptionMonthAndSubscriptionYear(
                        String enrollmentId, Integer month, Integer year);

        // By status
        Page<LmsStudentSubscription> findByStatus(SubscriptionStatus status, Pageable pageable);

        // By month/year
        List<LmsStudentSubscription> findBySubscriptionMonthAndSubscriptionYear(Integer month, Integer year);

        // Over limit
        List<LmsStudentSubscription> findByStatusAndAttendedSessionsGreaterThan(
                        SubscriptionStatus status, Integer allowedSessions);
}
