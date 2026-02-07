package com.artacademy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * Only students with APPROVED enrollment can have subscriptions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a monthly subscription for an approved student")
public class LmsSubscriptionRequestDto {

    @NotBlank(message = "Enrollment ID is required")
    @Schema(description = "ID of the APPROVED enrollment", example = "enrollment123abc")
    private String enrollmentId;

    @NotNull(message = "Subscription month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    @Schema(description = "Month of subscription (1-12)", example = "2")
    private Integer subscriptionMonth;

    @NotNull(message = "Subscription year is required")
    @Min(value = 2020, message = "Year must be 2020 or later")
    @Schema(description = "Year of subscription", example = "2026")
    private Integer subscriptionYear;

    @Builder.Default
    @Schema(description = "Number of sessions allowed per month", example = "8", defaultValue = "8")
    private Integer allowedSessions = 8;

    @Schema(description = "Optional notes", example = "First month subscription")
    private String notes;
}
