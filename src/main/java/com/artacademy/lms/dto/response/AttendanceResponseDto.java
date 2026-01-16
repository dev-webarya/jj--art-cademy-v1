package com.artacademy.lms.dto.response;

import com.artacademy.lms.enums.AttendanceStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class AttendanceResponseDto {

    private UUID id;
    private UUID sessionId;
    private String sessionTitle;
    private UUID batchStudentId;
    private String studentName;
    private String studentEmail;
    private AttendanceStatus status;
    private String markedByName;
    private Instant markedAt;
    private String remarks;
}
