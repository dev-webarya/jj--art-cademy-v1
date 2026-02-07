package com.artacademy.repository;

import com.artacademy.entity.LmsEvent;
import com.artacademy.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface LmsEventRepository extends MongoRepository<LmsEvent, String> {

    Page<LmsEvent> findByIsDeletedFalse(Pageable pageable);

    Page<LmsEvent> findByEventTypeAndIsDeletedFalse(EventType eventType, Pageable pageable);

    List<LmsEvent> findByStartDateTimeBetweenAndIsDeletedFalse(Instant start, Instant end);

    Page<LmsEvent> findByStartDateTimeGreaterThanEqualAndIsDeletedFalse(Instant now, Pageable pageable);

    Page<LmsEvent> findByIsPublicTrueAndIsDeletedFalse(Pageable pageable);

    Page<LmsEvent> findByIsRegistrationOpenTrueAndIsDeletedFalse(Pageable pageable);
}
