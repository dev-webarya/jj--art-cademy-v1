package com.artacademy.entity;

import com.artacademy.enums.EventType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * LmsEvent - Workshops, exhibitions, competitions managed by admin.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_events")
public class LmsEvent {

    @Id
    private String id;

    private String title;
    private String description;

    private EventType eventType;

    @Indexed
    private Instant startDateTime;

    private Instant endDateTime;

    private String location;

    @Builder.Default
    private Boolean isOnline = false;

    private String meetingLink;
    private String meetingPassword;

    private String imageUrl;
    private String bannerUrl;

    private Integer maxParticipants;

    @Builder.Default
    private Integer currentParticipants = 0;

    @Builder.Default
    private Boolean isPublic = true;

    @Builder.Default
    private Boolean isRegistrationOpen = true;

    private Double fee;

    private String createdBy;

    @Builder.Default
    private Boolean isDeleted = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper
    public boolean hasAvailableSlots() {
        return maxParticipants == null || currentParticipants < maxParticipants;
    }
}
