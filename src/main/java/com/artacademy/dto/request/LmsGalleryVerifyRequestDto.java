package com.artacademy.dto.request;

import com.artacademy.enums.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for verifying gallery items.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsGalleryVerifyRequestDto {

    @NotNull(message = "Verification status is required")
    private VerificationStatus status;

    private String rejectionReason; // Required if status is REJECTED

    private Boolean isFeatured;
}
