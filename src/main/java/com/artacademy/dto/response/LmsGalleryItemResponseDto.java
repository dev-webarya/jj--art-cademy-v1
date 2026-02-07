package com.artacademy.dto.response;

import com.artacademy.enums.LmsMediaType;
import com.artacademy.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for gallery item data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsGalleryItemResponseDto {

    private String id;

    private String uploadedBy;
    private String uploaderName;
    private String uploaderRole;

    private String title;
    private String description;

    private String mediaUrl;
    private LmsMediaType mediaType;
    private String thumbnailUrl;

    private String classId;
    private String className;
    private String batchId;

    private List<String> tags;

    private VerificationStatus verificationStatus;
    private String verifiedBy;
    private String verifiedByName;
    private Instant verifiedAt;
    private String rejectionReason;

    private Boolean isPublic;
    private Boolean isFeatured;

    private Integer viewCount;
    private Integer likeCount;

    private Instant createdAt;
    private Instant updatedAt;
}
