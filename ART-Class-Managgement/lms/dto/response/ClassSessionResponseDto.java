package com.artacademy.lms.dto.response;

import com.artacademy.lms.enums.MeetingPlatform;
import com.artacademy.lms.enums.SessionStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ClassSessionResponseDto {

    private UUID id;
    private UUID batchId;
    private String batchName;
    private String title;
    private String description;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String meetingLink;
    private MeetingPlatform meetingPlatform;
    private SessionStatus status;
    private String notes;
    private String recordingLink;
    private int attendanceCount;
    private Instant createdAt;
    private Instant updatedAt;
}
