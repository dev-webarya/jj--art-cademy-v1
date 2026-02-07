package com.artacademy.entity;

import com.artacademy.enums.LmsMediaType;
import com.artacademy.enums.VerificationStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * LmsGalleryItem - Student/Instructor artwork uploads requiring admin
 * verification.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_gallery")
public class LmsGalleryItem {

    @Id
    private String id;

    @Indexed
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

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    @Indexed
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private String verifiedBy;
    private String verifiedByName;
    private Instant verifiedAt;

    private String rejectionReason;

    @Builder.Default
    private Boolean isPublic = false;

    @Builder.Default
    private Boolean isFeatured = false;

    @Builder.Default
    private Integer viewCount = 0;

    @Builder.Default
    private Integer likeCount = 0;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public void approve(String adminId, String adminName) {
        this.verificationStatus = VerificationStatus.APPROVED;
        this.verifiedBy = adminId;
        this.verifiedByName = adminName;
        this.verifiedAt = Instant.now();
        this.isPublic = true;
    }

    public void reject(String adminId, String adminName, String reason) {
        this.verificationStatus = VerificationStatus.REJECTED;
        this.verifiedBy = adminId;
        this.verifiedByName = adminName;
        this.verifiedAt = Instant.now();
        this.rejectionReason = reason;
        this.isPublic = false;
    }
}
