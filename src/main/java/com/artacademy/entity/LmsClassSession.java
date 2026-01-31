package com.artacademy.entity;

import com.artacademy.enums.SessionStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * LmsClassSession - Individual class session created by admin.
 * When attendance is taken, attendance records are embedded for each student.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_class_sessions")
public class LmsClassSession {

    @Id
    private String id;

    @Indexed
    private LocalDate sessionDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private String topic;
    private String description;

    private String meetingLink;
    private String meetingPassword;

    @Builder.Default
    private SessionStatus status = SessionStatus.SCHEDULED;

    private String cancellationReason;

    // Attendance summary
    @Builder.Default
    private Integer totalStudents = 0;

    @Builder.Default
    private Integer presentCount = 0;

    @Builder.Default
    private Integer absentCount = 0;

    @Builder.Default
    private Boolean attendanceTaken = false;

    private Instant attendanceTakenAt;

    // Embedded attendance records
    @Builder.Default
    private List<SessionAttendanceRecord> attendanceRecords = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public void updateAttendanceSummary(int present, int absent, int total) {
        this.presentCount = present;
        this.absentCount = absent;
        this.totalStudents = total;
        this.attendanceTaken = true;
        this.attendanceTakenAt = Instant.now();
    }

    /**
     * Embedded attendance record for a single student in this session.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionAttendanceRecord {
        private String studentId;
        private String studentName;
        private String studentEmail;
        private String subscriptionId;

        private Boolean isPresent;

        private Integer sessionCountThisMonth;
        private Boolean isOverLimit;

        private String remarks;
        private Instant markedAt;
    }
}
