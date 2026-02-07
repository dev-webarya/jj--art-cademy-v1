package com.artacademy.repository;

import com.artacademy.entity.LmsClassSession;
import com.artacademy.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LmsClassSessionRepository extends MongoRepository<LmsClassSession, String> {

    List<LmsClassSession> findBySessionDateBetween(LocalDate start, LocalDate end);

    List<LmsClassSession> findBySessionDate(LocalDate date);

    Page<LmsClassSession> findBySessionDateGreaterThanEqual(LocalDate date, Pageable pageable);

    Page<LmsClassSession> findByStatus(SessionStatus status, Pageable pageable);

    List<LmsClassSession> findByStatusAndSessionDateLessThan(SessionStatus status, LocalDate date);
}
