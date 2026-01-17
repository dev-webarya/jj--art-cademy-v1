package com.artacademy.dto.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRequest {

    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotBlank(message = "Start time is required")
    private String startTime; // e.g., "10:00 AM"

    @NotBlank(message = "End time is required")
    private String endTime; // e.g., "12:00 PM"

    private String remarks;
}
