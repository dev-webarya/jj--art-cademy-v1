package com.artacademy.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubmissionRequestDto {

    @NotBlank(message = "Submission link is required")
    private String submissionLink; // Google Drive folder link

    private String studentNotes;
}
