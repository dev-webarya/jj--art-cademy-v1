package com.artacademy.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating/updating student subscriptions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsSubscriptionRequestDto {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Subscription month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer subscriptionMonth;

    @NotNull(message = "Subscription year is required")
    @Min(value = 2020, message = "Year must be 2020 or later")
    private Integer subscriptionYear;

    @Builder.Default
    private Integer allowedSessions = 8;

    private String paymentId;
    private Double amountPaid;
    private String notes;
}
