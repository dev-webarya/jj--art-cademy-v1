package com.artacademy.lms.dto.request;

import com.artacademy.lms.enums.MeetingPlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ClassSessionRequestDto {

    @NotNull(message = "Batch ID is required")
    private UUID batchId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private String meetingLink;

    private MeetingPlatform meetingPlatform = MeetingPlatform.ZOOM;

    private String notes;
}
