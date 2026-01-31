package com.artacademy.entity;

import com.artacademy.enums.SubscriptionStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/**
 * LmsStudentSubscription - Monthly subscription for a student.
 * Students can attend up to 8 sessions per month (configurable).
 * No carry-over to next month.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_subscriptions")
@CompoundIndex(name = "student_month_year_idx", def = "{'studentId': 1, 'subscriptionMonth': 1, 'subscriptionYear': 1}", unique = true)
public class LmsStudentSubscription {

    @Id
    private String id;

    @Indexed
    private String studentId;

    private String studentName;
    private String studentEmail;
    private String studentPhone;

    // Subscription period
    private Integer subscriptionMonth; // 1-12
    private Integer subscriptionYear; // e.g., 2026
    private LocalDate startDate;
    private LocalDate endDate;

    // Session limits
    @Builder.Default
    private Integer allowedSessions = 8;

    @Builder.Default
    private Integer attendedSessions = 0;

    @Builder.Default
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    private String paymentId;
    private Double amountPaid;
    private String notes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public boolean hasSessionsRemaining() {
        return attendedSessions < allowedSessions;
    }

    public boolean isOverLimit() {
        return attendedSessions > allowedSessions;
    }

    public void incrementAttendedSessions() {
        this.attendedSessions++;
    }

    public void decrementAttendedSessions() {
        if (this.attendedSessions > 0) {
            this.attendedSessions--;
        }
    }

    public int getRemainingSessionCount() {
        return Math.max(0, allowedSessions - attendedSessions);
    }
}
