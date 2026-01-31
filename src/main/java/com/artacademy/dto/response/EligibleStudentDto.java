package com.artacademy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for students eligible for attendance.
 * Only includes students with APPROVED enrollment and active subscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Student eligible for attendance marking")
public class EligibleStudentDto {

    @Schema(description = "User ID", example = "user123abc")
    private String studentId;

    @Schema(description = "Enrollment ID", example = "enrollment123abc")
    private String enrollmentId;

    @Schema(description = "Subscription ID", example = "sub123abc")
    private String subscriptionId;

    @Schema(description = "Roll number", example = "AA-0001")
    private String rollNo;

    @Schema(description = "Student's full name", example = "Alice Artist")
    private String studentName;

    @Schema(description = "Student's email", example = "alice@test.com")
    private String studentEmail;

    @Schema(description = "Student's phone", example = "+911231231234")
    private String studentPhone;

    @Schema(description = "Sessions attended this month", example = "3")
    private Integer attendedSessions;

    @Schema(description = "Allowed sessions per month", example = "8")
    private Integer allowedSessions;

    @Schema(description = "Remaining sessions", example = "5")
    private Integer remainingSessions;

    @Schema(description = "Has exceeded session limit?", example = "false")
    private Boolean isOverLimit;
}
