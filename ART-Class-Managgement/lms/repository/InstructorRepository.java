package com.artacademy.lms.repository;

import com.artacademy.entity.User;
import com.artacademy.lms.entity.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    Optional<Instructor> findByUser(User user);

    Optional<Instructor> findByUserId(UUID userId);

    boolean existsByUser(User user);

    boolean existsByUserId(UUID userId);

    Page<Instructor> findByIsActiveTrue(Pageable pageable);

    List<Instructor> findByIsActiveTrue();

    @Query("SELECT i FROM Instructor i WHERE i.deleted = false")
    List<Instructor> findAllActive();
}
