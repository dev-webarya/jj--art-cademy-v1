package com.artacademy.lms.entity;

import com.artacademy.entity.ArtClasses;
import com.artacademy.enums.ClassSchedule;
import com.artacademy.lms.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A batch represents a specific instance of a class with:
 * - One instructor (can be null if instructor removed)
 * - Multiple students
 * - Specific schedule/timing
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lms_batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_class_id", nullable = false)
    private ArtClasses artClass;

    // Nullable - instructor can be removed (soft deleted) and batch needs
    // reassignment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassSchedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BatchStatus status = BatchStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxStudents = 20;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<BatchStudent> students = new HashSet<>();

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ClassSession> sessions = new HashSet<>();

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Assignment> assignments = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    // Helper method to check if batch needs instructor
    public boolean needsInstructor() {
        return instructor == null || status == BatchStatus.NEEDS_INSTRUCTOR;
    }
}
