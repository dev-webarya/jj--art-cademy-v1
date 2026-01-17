package com.artacademy.lms.repository;

import com.artacademy.entity.ArtClasses;
import com.artacademy.lms.entity.Batch;
import com.artacademy.lms.entity.Instructor;
import com.artacademy.lms.enums.BatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID>, JpaSpecificationExecutor<Batch> {

    Page<Batch> findByInstructor(Instructor instructor, Pageable pageable);

    List<Batch> findByInstructor(Instructor instructor);

    Page<Batch> findByArtClass(ArtClasses artClass, Pageable pageable);

    Page<Batch> findByStatus(BatchStatus status, Pageable pageable);

    List<Batch> findByStatus(BatchStatus status);

    // Count batches needing instructor reassignment
    long countByStatus(BatchStatus status);

    // Update all batches when instructor is deleted
    @Modifying
    @Query("UPDATE Batch b SET b.instructor = null, b.status = :newStatus WHERE b.instructor.id = :instructorId")
    int unassignInstructorFromBatches(@Param("instructorId") UUID instructorId,
            @Param("newStatus") BatchStatus newStatus);
}
