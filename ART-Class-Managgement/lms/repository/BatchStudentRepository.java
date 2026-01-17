package com.artacademy.lms.repository;

import com.artacademy.entity.User;
import com.artacademy.lms.entity.Batch;
import com.artacademy.lms.entity.BatchStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatchStudentRepository extends JpaRepository<BatchStudent, UUID> {

    List<BatchStudent> findByBatch(Batch batch);

    Page<BatchStudent> findByBatch(Batch batch, Pageable pageable);

    List<BatchStudent> findByUser(User user);

    Page<BatchStudent> findByUser(User user, Pageable pageable);

    Optional<BatchStudent> findByBatchAndUser(Batch batch, User user);

    boolean existsByBatchAndUser(Batch batch, User user);

    long countByBatch(Batch batch);

    long countByBatchAndIsActiveTrue(Batch batch);
}
