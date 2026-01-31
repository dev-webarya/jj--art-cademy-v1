package com.artacademy.entity;

import com.artacademy.enums.AttendanceStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/**
 * LmsAttendance - Per-student per-session attendance record.
 * Tracks monthly session count and over-limit status.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_attendance")
@CompoundIndex(name = "session_student_idx", def = "{'sessionId': 1, 'studentId': 1}", unique = true)
@CompoundIndex(name = "student_month_year_idx", def = "{'studentId': 1, 'sessionMonth': 1, 'sessionYear': 1}")
public class LmsAttendance {

    @Id
    private String id;

    @Indexed
    private String sessionId;

    @Indexed
    private String studentId;

    private String studentName;
    private String studentEmail;

    @Indexed
    private String subscriptionId;

    // Session date info
    private LocalDate sessionDate;
    private Integer sessionMonth; // 1-12
    private Integer sessionYear; // e.g., 2026

    // Monthly session tracking
    @Builder.Default
    private Integer sessionCountThisMonth = 0;

    @Builder.Default
    private Boolean isOverLimit = false;

    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.ABSENT;

    private Instant markedAt;
    private String remarks;

    @CreatedDate
    private Instant createdAt;

    // Helper methods
    public void markPresent() {
        this.status = AttendanceStatus.PRESENT;
        this.markedAt = Instant.now();
    }

    public void markAbsent() {
        this.status = AttendanceStatus.ABSENT;
        this.markedAt = Instant.now();
    }

    public void updateSessionCount(int count, int limit) {
        this.sessionCountThisMonth = count;
        this.isOverLimit = count > limit;
    }
}
