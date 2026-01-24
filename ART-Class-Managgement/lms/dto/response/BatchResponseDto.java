package com.artacademy.lms.dto.response;

import com.artacademy.enums.ClassSchedule;
import com.artacademy.lms.enums.BatchStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BatchResponseDto {

    private UUID id;
    private String name;
    private UUID artClassId;
    private String artClassName;
    private UUID instructorId;
    private String instructorName;
    private ClassSchedule schedule;
    private String scheduleDisplayName;
    private BatchStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxStudents;
    private int currentStudentCount;
    private String description;
    private boolean needsInstructor;
    private Instant createdAt;
    private Instant updatedAt;
}
