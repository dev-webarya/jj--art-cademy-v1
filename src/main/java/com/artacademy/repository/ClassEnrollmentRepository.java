package com.artacademy.repository;

import com.artacademy.entity.ArtClasses;
import com.artacademy.entity.ClassEnrollment;
import com.artacademy.entity.User;
import com.artacademy.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, UUID> {

    // Find all enrollments for a specific user
    List<ClassEnrollment> findByUser(User user);

    // Find enrollments by user with pagination
    Page<ClassEnrollment> findByUser(User user, Pageable pageable);

    // Find enrollments by status (for admin)
    Page<ClassEnrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    // Find all enrollments for a specific class
    List<ClassEnrollment> findByArtClass(ArtClasses artClass);

    // Check if user already enrolled in a specific class
    boolean existsByUserAndArtClass(User user, ArtClasses artClass);

    // Count enrollments by status
    long countByStatus(EnrollmentStatus status);

    // Count enrollments for a specific class
    long countByArtClass(ArtClasses artClass);
}
