package com.artacademy.repository;

import com.artacademy.entity.AttendanceRecord;
import com.artacademy.enums.AttendanceStatus;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends MongoRepository<AttendanceRecord, String> {

    @Query("{'session.sessionId': ?0}")
    List<AttendanceRecord> findBySessionId(String sessionId);

    @Query("{'student.userId': ?0}")
    List<AttendanceRecord> findByStudentId(String studentId);

    @Query("{'artClass.classId': ?0}")
    List<AttendanceRecord> findByClassId(String classId);

    @Query("{'session.sessionId': ?0, 'student.userId': ?1}")
    Optional<AttendanceRecord> findBySessionIdAndStudentId(String sessionId, String studentId);

    @Query("{'student.userId': ?0, 'session.sessionDate': {$gte: ?1, $lte: ?2}}")
    List<AttendanceRecord> findByStudentIdAndDateRange(String studentId, LocalDate startDate, LocalDate endDate);

    @Query("{'artClass.classId': ?0, 'session.sessionDate': {$gte: ?1, $lte: ?2}}")
    List<AttendanceRecord> findByClassIdAndDateRange(String classId, LocalDate startDate, LocalDate endDate);

    List<AttendanceRecord> findByStatus(AttendanceStatus status);

    @Query("{'session.sessionId': ?0, 'status': ?1}")
    List<AttendanceRecord> findBySessionIdAndStatus(String sessionId, AttendanceStatus status);

    // Count attendance by status for a session
    @Query(value = "{'session.sessionId': ?0, 'status': ?1}", count = true)
    long countBySessionIdAndStatus(String sessionId, AttendanceStatus status);

    // Count total attendance for a student
    @Query(value = "{'student.userId': ?0}", count = true)
    long countByStudentId(String studentId);

    // Count present attendance for a student
    @Query(value = "{'student.userId': ?0, 'status': 'PRESENT'}", count = true)
    long countPresentByStudentId(String studentId);
}
