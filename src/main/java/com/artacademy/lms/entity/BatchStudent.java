package com.artacademy.lms.entity;

import com.artacademy.entity.ClassEnrollment;
import com.artacademy.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Links a student (User) to a Batch.
 * Created when admin adds approved enrollment to a batch.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lms_batch_students", uniqueConstraints = @UniqueConstraint(columnNames = { "batch_id", "user_id" }))
public class BatchStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Optional link to the original enrollment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private ClassEnrollment enrollment;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @OneToMany(mappedBy = "batchStudent", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Attendance> attendanceRecords = new HashSet<>();

    @OneToMany(mappedBy = "batchStudent", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AssignmentSubmission> submissions = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant joinedAt;
}
