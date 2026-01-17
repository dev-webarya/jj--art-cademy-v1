package com.artacademy.lms.repository;

import com.artacademy.lms.entity.Assignment;
import com.artacademy.lms.entity.Batch;
import com.artacademy.lms.entity.Instructor;
import com.artacademy.lms.enums.AssignmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    Page<Assignment> findByBatch(Batch batch, Pageable pageable);

    List<Assignment> findByBatch(Batch batch);

    List<Assignment> findByBatchAndStatus(Batch batch, AssignmentStatus status);

    Page<Assignment> findByCreatedBy(Instructor instructor, Pageable pageable);

    // Due soon assignments
    List<Assignment> findByBatchAndStatusAndDueDateBefore(Batch batch, AssignmentStatus status, LocalDateTime dueDate);

    long countByBatch(Batch batch);

    long countByBatchAndStatus(Batch batch, AssignmentStatus status);
}
