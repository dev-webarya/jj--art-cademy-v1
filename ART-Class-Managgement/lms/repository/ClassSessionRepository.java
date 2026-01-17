package com.artacademy.lms.repository;

import com.artacademy.lms.entity.Batch;
import com.artacademy.lms.entity.ClassSession;
import com.artacademy.lms.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, UUID> {

    Page<ClassSession> findByBatch(Batch batch, Pageable pageable);

    List<ClassSession> findByBatch(Batch batch);

    List<ClassSession> findByBatchAndSessionDate(Batch batch, LocalDate sessionDate);

    Page<ClassSession> findByStatus(SessionStatus status, Pageable pageable);

    List<ClassSession> findBySessionDateAndStatus(LocalDate date, SessionStatus status);

    // Upcoming sessions for a batch
    List<ClassSession> findByBatchAndSessionDateAfterOrderBySessionDateAsc(Batch batch, LocalDate date);

    long countByBatch(Batch batch);

    long countByBatchAndStatus(Batch batch, SessionStatus status);
}
