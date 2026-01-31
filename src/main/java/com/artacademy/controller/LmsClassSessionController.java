package com.artacademy.controller;

import com.artacademy.dto.request.LmsClassSessionRequestDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.enums.SessionStatus;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsClassSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing class sessions.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/v1/lms/sessions")
@RequiredArgsConstructor
@Tag(name = "LMS - Class Sessions", description = "Class session management (Admin only)")
@AdminOnly
public class LmsClassSessionController {

    private final LmsClassSessionService sessionService;

    @PostMapping
    @Operation(summary = "Create a new class session")
    public ResponseEntity<LmsClassSessionResponseDto> create(@Valid @RequestBody LmsClassSessionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get session by ID")
    public ResponseEntity<LmsClassSessionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a session")
    public ResponseEntity<LmsClassSessionResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody LmsClassSessionRequestDto request) {
        return ResponseEntity.ok(sessionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a session")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all sessions (paginated)")
    public ResponseEntity<Page<LmsClassSessionResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(sessionService.getAll(pageable));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get sessions by date")
    public ResponseEntity<List<LmsClassSessionResponseDto>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(sessionService.getByDate(date));
    }

    @GetMapping("/range")
    @Operation(summary = "Get sessions by date range")
    public ResponseEntity<List<LmsClassSessionResponseDto>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(sessionService.getByDateRange(startDate, endDate));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming sessions")
    public ResponseEntity<Page<LmsClassSessionResponseDto>> getUpcoming(Pageable pageable) {
        return ResponseEntity.ok(sessionService.getUpcoming(pageable));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update session status")
    public ResponseEntity<LmsClassSessionResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam SessionStatus status,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(sessionService.updateStatus(id, status, reason));
    }

    @GetMapping("/{id}/attendance")
    @Operation(summary = "Get session with attendance records")
    public ResponseEntity<LmsClassSessionResponseDto> getSessionWithAttendance(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.getSessionWithAttendance(id));
    }
}
