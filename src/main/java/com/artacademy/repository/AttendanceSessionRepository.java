package com.artacademy.repository;

import com.artacademy.entity.AttendanceSession;
import com.artacademy.enums.SessionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends MongoRepository<AttendanceSession, String> {

    Optional<AttendanceSession> findBySessionCode(String sessionCode);

    @Query("{'artClass.classId': ?0}")
    List<AttendanceSession> findByClassId(String classId);

    @Query("{'teacher.userId': ?0}")
    List<AttendanceSession> findByTeacherId(String teacherId);

    List<AttendanceSession> findBySessionDate(LocalDate sessionDate);

    @Query("{'sessionDate': {$gte: ?0, $lte: ?1}}")
    List<AttendanceSession> findBySessionDateBetween(LocalDate startDate, LocalDate endDate);

    List<AttendanceSession> findByStatus(SessionStatus status);

    @Query("{'artClass.classId': ?0, 'sessionDate': ?1}")
    List<AttendanceSession> findByClassIdAndDate(String classId, LocalDate date);

    @Query("{'artClass.classId': ?0, 'status': ?1}")
    List<AttendanceSession> findByClassIdAndStatus(String classId, SessionStatus status);

    @Query("{'sessionDate': ?0, 'status': 'SCHEDULED'}")
    List<AttendanceSession> findScheduledSessionsForDate(LocalDate date);
}
