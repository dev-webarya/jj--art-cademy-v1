package com.artacademy.dto.response;

import com.artacademy.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response DTO for event data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsEventResponseDto {

    private String id;
    private String title;
    private String description;

    private EventType eventType;

    private Instant startDateTime;
    private Instant endDateTime;

    private String location;
    private Boolean isOnline;
    private String meetingLink;
    private String meetingPassword;

    private String imageUrl;
    private String bannerUrl;

    private Integer maxParticipants;
    private Integer currentParticipants;
    private Integer availableSlots;

    private Boolean isPublic;
    private Boolean isRegistrationOpen;
    private Boolean hasAvailableSlots;

    private Double fee;

    private String createdBy;

    private Instant createdAt;
    private Instant updatedAt;
}
