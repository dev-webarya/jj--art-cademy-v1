package com.artacademy.entity;

import com.artacademy.enums.ClassSchedule;
import com.artacademy.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "class_enrollments")
public class ClassEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // The student enrolling (from logged-in user)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The class being enrolled in
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_class_id", nullable = false)
    private ArtClasses artClass;

    @Column(nullable = false)
    private String parentGuardianName;

    @Column(nullable = false)
    private Integer studentAge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassSchedule schedule;

    @Column(columnDefinition = "TEXT")
    private String additionalMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String adminNotes;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
