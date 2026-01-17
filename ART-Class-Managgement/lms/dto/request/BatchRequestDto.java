package com.artacademy.lms.dto.request;

import com.artacademy.enums.ClassSchedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BatchRequestDto {

    @NotBlank(message = "Batch name is required")
    private String name;

    @NotNull(message = "Art class ID is required")
    private UUID artClassId;

    private UUID instructorId; // Optional - can assign later

    @NotNull(message = "Schedule is required")
    private ClassSchedule schedule;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Integer maxStudents = 20;

    private String description;
}
