package com.artacademy.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Student's submission for an assignment.
 * Links to Google Drive folder containing their work.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lms_assignment_submissions", uniqueConstraints = @UniqueConstraint(columnNames = { "assignment_id",
        "batch_student_id" }))
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_student_id", nullable = false)
    private BatchStudent batchStudent;

    // Google Drive folder link for submission
    @Column(nullable = false, columnDefinition = "TEXT")
    private String submissionLink;

    @Column(columnDefinition = "TEXT")
    private String studentNotes;

    @Column
    private Instant submittedAt;

    @Column
    private boolean isLate;

    // Grading
    @Column
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by_id")
    private Instructor gradedBy;

    @Column
    private Instant gradedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
