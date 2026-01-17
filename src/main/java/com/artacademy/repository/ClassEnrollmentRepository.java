package com.artacademy.repository;

import com.artacademy.entity.ClassEnrollment;
import com.artacademy.enums.EnrollmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassEnrollmentRepository extends MongoRepository<ClassEnrollment, String> {

    @Query("{'student.userId': ?0}")
    List<ClassEnrollment> findByStudentId(String studentId);

    @Query("{'artClass.classId': ?0}")
    List<ClassEnrollment> findByClassId(String classId);

    @Query("{'student.userId': ?0, 'artClass.classId': ?1}")
    Optional<ClassEnrollment> findByStudentIdAndClassId(String studentId, String classId);

    @Query("{'artClass.classId': ?0, 'status': 'ACTIVE'}")
    List<ClassEnrollment> findActiveEnrollmentsByClassId(String classId);

    @Query("{'student.userId': ?0, 'status': 'ACTIVE'}")
    List<ClassEnrollment> findActiveEnrollmentsByStudentId(String studentId);

    List<ClassEnrollment> findByStatus(EnrollmentStatus status);

    @Query(value = "{'artClass.classId': ?0, 'status': 'ACTIVE'}", count = true)
    long countActiveEnrollmentsByClassId(String classId);

    boolean existsByStudentUserIdAndArtClassClassId(String studentUserId, String classClassId);
}
