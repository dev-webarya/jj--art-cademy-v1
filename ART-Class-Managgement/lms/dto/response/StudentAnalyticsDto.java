package com.artacademy.lms.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Analytics data for a student
 */
@Data
@Builder
public class StudentAnalyticsDto {

    private String studentName;
    private String studentEmail;
    private String batchName;
    private String className;

    // Attendance stats
    private long totalSessions;
    private long sessionsAttended;
    private double attendancePercentage;

    // Assignment stats
    private long totalAssignments;
    private long submittedAssignments;
    private long gradedAssignments;
    private Double averageScore;

    // Overall
    private String overallStatus; // GOOD, AVERAGE, NEEDS_IMPROVEMENT
}
