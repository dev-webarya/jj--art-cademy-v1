package com.artacademy.repository;

import com.artacademy.entity.ClassEnrollment;
import com.artacademy.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassEnrollmentRepository extends MongoRepository<ClassEnrollment, String> {

    Page<ClassEnrollment> findByUserId(String userId, Pageable pageable);

    Page<ClassEnrollment> findByClassId(String classId, Pageable pageable);

    List<ClassEnrollment> findByUserIdAndStatus(String userId, EnrollmentStatus status);

    Optional<ClassEnrollment> findByUserIdAndClassId(String userId, String classId);

    Page<ClassEnrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    List<ClassEnrollment> findByStatus(EnrollmentStatus status);

    long countByClassIdAndStatus(String classId, EnrollmentStatus status);

    long countByStatus(EnrollmentStatus status);

    boolean existsByUserIdAndClassIdAndStatusIn(String userId, String classId, List<EnrollmentStatus> statuses);
}
