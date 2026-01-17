package com.artacademy.entity;

import com.artacademy.enums.AttendanceStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "attendance_records")
@CompoundIndex(name = "session_student_idx", def = "{'session.sessionId': 1, 'student.userId': 1}", unique = true)
public class AttendanceRecord {

    @Id
    private String id;

    // Embedded session reference
    private SessionRef session;

    // Embedded art class reference
    private ArtClassRef artClass;

    // Embedded student reference
    private StudentRef student;

    @Indexed
    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.ABSENT;

    private Instant checkInTime;
    private Instant checkOutTime;

    // Who marked the attendance
    private MarkedByRef markedBy;

    private String remarks;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Embedded class for session reference
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionRef {
        private String sessionId;
        private String sessionCode;
        private LocalDate sessionDate;
    }

    // Embedded class for art class reference
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtClassRef {
        private String classId;
        private String name;
    }

    // Embedded class for student reference
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentRef {
        private String userId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
    }

    // Embedded class for marked by reference
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarkedByRef {
        private String userId;
        private String firstName;
        private String lastName;
        private String role; // "TEACHER" or "ADMIN"
    }
}
