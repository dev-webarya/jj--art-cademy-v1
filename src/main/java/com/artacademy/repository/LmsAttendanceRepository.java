package com.artacademy.repository;

import com.artacademy.entity.LmsAttendance;
import com.artacademy.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LmsAttendanceRepository extends MongoRepository<LmsAttendance, String> {

        List<LmsAttendance> findBySessionId(String sessionId);

        Page<LmsAttendance> findByStudentId(String studentId, Pageable pageable);

        List<LmsAttendance> findByStudentIdOrderBySessionDateDesc(String studentId);

        Optional<LmsAttendance> findBySessionIdAndStudentId(String sessionId, String studentId);

        List<LmsAttendance> findByStudentIdAndSessionMonthAndSessionYear(
                        String studentId, Integer month, Integer year);

        List<LmsAttendance> findBySessionMonthAndSessionYearAndIsOverLimitTrue(
                        Integer month, Integer year);

        long countByStudentIdAndSessionMonthAndSessionYearAndStatus(
                        String studentId, Integer month, Integer year, AttendanceStatus status);

        boolean existsBySessionIdAndStudentId(String sessionId, String studentId);
}
