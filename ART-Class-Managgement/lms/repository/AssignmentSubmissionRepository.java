package com.artacademy.lms.repository;

import com.artacademy.lms.entity.Assignment;
import com.artacademy.lms.entity.AssignmentSubmission;
import com.artacademy.lms.entity.BatchStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, UUID> {

    Page<AssignmentSubmission> findByAssignment(Assignment assignment, Pageable pageable);

    List<AssignmentSubmission> findByAssignment(Assignment assignment);

    List<AssignmentSubmission> findByBatchStudent(BatchStudent batchStudent);

    Optional<AssignmentSubmission> findByAssignmentAndBatchStudent(Assignment assignment, BatchStudent batchStudent);

    boolean existsByAssignmentAndBatchStudent(Assignment assignment, BatchStudent batchStudent);

    // Graded vs ungraded
    long countByAssignmentAndScoreIsNotNull(Assignment assignment);

    long countByAssignmentAndScoreIsNull(Assignment assignment);

    // Average score for assignment
    @Query("SELECT AVG(s.score) FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.score IS NOT NULL")
    Double getAverageScoreForAssignment(@Param("assignment") Assignment assignment);

    // Student's average score across all submissions
    @Query("SELECT AVG(s.score) FROM AssignmentSubmission s WHERE s.batchStudent = :student AND s.score IS NOT NULL")
    Double getAverageScoreForStudent(@Param("student") BatchStudent student);
}
