package com.artacademy.service;

import com.artacademy.dto.request.LmsClassSessionRequestDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing class sessions.
 * Admin-only operations.
 */
public interface LmsClassSessionService {

    // CRUD operations
    LmsClassSessionResponseDto create(LmsClassSessionRequestDto request);

    LmsClassSessionResponseDto getById(String id);

    LmsClassSessionResponseDto update(String id, LmsClassSessionRequestDto request);

    void delete(String id);

    // Query operations
    Page<LmsClassSessionResponseDto> getAll(Pageable pageable);

    List<LmsClassSessionResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate);

    List<LmsClassSessionResponseDto> getByDate(LocalDate date);

    Page<LmsClassSessionResponseDto> getUpcoming(Pageable pageable);

    // Status management
    LmsClassSessionResponseDto updateStatus(String id, SessionStatus status, String reason);

    // Attendance-related
    LmsClassSessionResponseDto getSessionWithAttendance(String id);
}
