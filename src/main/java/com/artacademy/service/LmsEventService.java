package com.artacademy.service;

import com.artacademy.dto.request.LmsEventRequestDto;
import com.artacademy.dto.response.LmsEventResponseDto;
import com.artacademy.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing events.
 * Admin-only operations.
 */
public interface LmsEventService {

    // CRUD operations
    LmsEventResponseDto create(LmsEventRequestDto request);

    LmsEventResponseDto getById(String id);

    LmsEventResponseDto update(String id, LmsEventRequestDto request);

    void delete(String id);

    // Query operations
    Page<LmsEventResponseDto> getAll(Pageable pageable);

    Page<LmsEventResponseDto> getByType(EventType eventType, Pageable pageable);

    Page<LmsEventResponseDto> getUpcoming(Pageable pageable);

    Page<LmsEventResponseDto> getPublicEvents(Pageable pageable);
}
