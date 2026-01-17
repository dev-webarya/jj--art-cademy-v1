package com.artacademy.entity;

import com.artacademy.enums.EnrollmentStatus;
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
@Document(collection = "class_enrollments")
@CompoundIndex(name = "student_class_idx", def = "{'student.userId': 1, 'artClass.classId': 1}", unique = true)
public class ClassEnrollment {

    @Id
    private String id;

    // Embedded student reference
    private StudentRef student;

    // Embedded art class reference
    private ArtClassRef artClass;

    private LocalDate enrollmentDate;

    @Indexed
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    private String paymentStatus; // "PAID", "PENDING", "PARTIAL"

    private LocalDate startDate;
    private LocalDate endDate;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

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
        private String proficiency;
    }
}
