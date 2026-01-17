package com.artacademy.entity;

import com.artacademy.enums.ClassSchedule;
import com.artacademy.enums.EnrollmentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "enrollments")
@CompoundIndex(name = "user_class_idx", def = "{'userId': 1, 'artClassId': 1}")
public class ClassEnrollment {

    @Id
    private String id;

    // The student enrolling (from logged-in user)
    @Indexed
    private String userId;

    private String userEmail;

    // The class being enrolled in
    @Indexed
    private String classId;

    private String className;

    private String classDescription;

    private String studentName;

    private String parentGuardianName;

    private Integer studentAge;

    private ClassSchedule schedule;

    private String additionalMessage;

    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    private String adminNotes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
