package com.artacademy.dto.request;

import com.artacademy.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Request DTO for creating/updating events.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsEventRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotNull(message = "Start date/time is required")
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
    private Boolean isPublic = true;

    @Builder.Default
    private Boolean isRegistrationOpen = true;

    private Double fee;
}
