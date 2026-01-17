package com.artacademy.lms.repository;

import com.artacademy.lms.entity.Attendance;
import com.artacademy.lms.entity.BatchStudent;
import com.artacademy.lms.entity.ClassSession;
import com.artacademy.lms.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    List<Attendance> findBySession(ClassSession session);

    List<Attendance> findByBatchStudent(BatchStudent batchStudent);

    Optional<Attendance> findBySessionAndBatchStudent(ClassSession session, BatchStudent batchStudent);

    boolean existsBySessionAndBatchStudent(ClassSession session, BatchStudent batchStudent);

    long countBySessionAndStatus(ClassSession session, AttendanceStatus status);

    // Attendance stats per student
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.batchStudent = :student AND a.status = :status")
    long countByBatchStudentAndStatus(@Param("student") BatchStudent student, @Param("status") AttendanceStatus status);

    // Get attendance percentage for a student in a batch
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.batchStudent = :student")
    long countTotalAttendanceForStudent(@Param("student") BatchStudent student);
}
