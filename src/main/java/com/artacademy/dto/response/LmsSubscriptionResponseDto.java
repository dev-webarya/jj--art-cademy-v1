package com.artacademy.dto.response;

import com.artacademy.enums.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Response DTO for subscription data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Monthly subscription details for a student")
public class LmsSubscriptionResponseDto {

    @Schema(description = "Subscription ID", example = "sub123abc")
    private String id;

    @Schema(description = "Enrollment ID", example = "enrollment123abc")
    private String enrollmentId;

    @Schema(description = "Student's roll number", example = "AA-0001")
    private String rollNo;

    @Schema(description = "User ID", example = "user123abc")
    private String studentId;

    @Schema(description = "Student's full name", example = "Alice Artist")
    private String studentName;

    @Schema(description = "Student's email", example = "alice@test.com")
    private String studentEmail;

    @Schema(description = "Student's phone", example = "+911231231234")
    private String studentPhone;

    @Schema(description = "Subscription month (1-12)", example = "2")
    private Integer subscriptionMonth;

    @Schema(description = "Subscription year", example = "2026")
    private Integer subscriptionYear;

    @Schema(description = "Start date of subscription period", example = "2026-02-01")
    private LocalDate startDate;

    @Schema(description = "End date of subscription period", example = "2026-02-28")
    private LocalDate endDate;

    @Schema(description = "Maximum sessions allowed", example = "8")
    private Integer allowedSessions;

    @Schema(description = "Sessions attended so far", example = "3")
    private Integer attendedSessions;

    @Schema(description = "Remaining sessions", example = "5")
    private Integer remainingSessions;

    @Schema(description = "Has student exceeded limit?", example = "false")
    private Boolean isOverLimit;

    @Schema(description = "Subscription status", example = "ACTIVE")
    private SubscriptionStatus status;

    @Schema(description = "Notes", example = "First month subscription")
    private String notes;

    @Schema(description = "Created timestamp")
    private Instant createdAt;

    @Schema(description = "Last updated timestamp")
    private Instant updatedAt;
}
