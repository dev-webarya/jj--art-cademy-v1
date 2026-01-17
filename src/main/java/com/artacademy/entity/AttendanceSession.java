package com.artacademy.entity;

import com.artacademy.enums.SessionStatus;
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
@Document(collection = "attendance_sessions")
@CompoundIndex(name = "class_date_idx", def = "{'artClass.classId': 1, 'sessionDate': 1}")
public class AttendanceSession {

    @Id
    private String id;

    @Indexed(unique = true)
    private String sessionCode; // e.g., "ART101-2026-01-17-AM"

    // Embedded art class reference
    private ArtClassRef artClass;

    // Embedded teacher reference
    private TeacherRef teacher;

    @Indexed
    private LocalDate sessionDate;

    private String startTime; // "10:00 AM"
    private String endTime; // "12:00 PM"

    @Indexed
    @Builder.Default
    private SessionStatus status = SessionStatus.SCHEDULED;

    @Builder.Default
    private Integer totalEnrolled = 0;

    @Builder.Default
    private Integer totalPresent = 0;

    @Builder.Default
    private Integer totalAbsent = 0;

    private String remarks;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Embedded class for art class reference
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtClassRef {
        private String classId;
        private String name;
        private String proficiency;
    }

    // Embedded class for teacher reference
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherRef {
        private String userId;
        private String firstName;
        private String lastName;
        private String email;
    }
}
