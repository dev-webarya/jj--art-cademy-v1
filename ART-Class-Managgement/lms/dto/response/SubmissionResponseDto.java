package com.artacademy.lms.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class SubmissionResponseDto {

    private UUID id;
    private UUID assignmentId;
    private String assignmentTitle;
    private UUID batchStudentId;
    private String studentName;
    private String studentEmail;
    private String submissionLink;
    private String studentNotes;
    private Instant submittedAt;
    private boolean isLate;
    private Integer score;
    private Integer maxScore;
    private String feedback;
    private String gradedByName;
    private Instant gradedAt;
}
