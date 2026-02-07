package com.artacademy.entity;

import com.artacademy.enums.SubscriptionStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;

/**
 * LmsStudentSubscription - Monthly subscription for a student.
 * Students can attend up to 8 sessions per month (configurable).
 * No carry-over to next month.
 * Only students with APPROVED enrollment can have subscriptions.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_subscriptions")
@CompoundIndex(name = "student_month_year_idx", def = "{'enrollmentId': 1, 'subscriptionMonth': 1, 'subscriptionYear': 1}", unique = true)
public class LmsStudentSubscription {

    @Id
    private String id;

    // --- Link to APPROVED Enrollment ---
    @Indexed
    @Field("enrollment_id")
    private String enrollmentId;

    @Field("roll_no")
    private String rollNo;

    // --- Student Info (denormalized from enrollment) ---
    @Field("student_id")
    private String studentId;

    @Field("student_name")
    private String studentName;

    @Field("student_email")
    private String studentEmail;

    @Field("student_phone")
    private String studentPhone;

    // --- Subscription period ---
    @Field("subscription_month")
    private Integer subscriptionMonth; // 1-12

    @Field("subscription_year")
    private Integer subscriptionYear; // e.g., 2026

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    // --- Session limits ---
    @Builder.Default
    @Field("allowed_sessions")
    private Integer allowedSessions = 8;

    @Builder.Default
    @Field("attended_sessions")
    private Integer attendedSessions = 0;

    @Builder.Default
    @Field("status")
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Field("notes")
    private String notes;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
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
