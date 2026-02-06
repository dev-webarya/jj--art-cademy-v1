package com.artacademy.controller;

import com.artacademy.dto.request.LmsClassSessionRequestDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.enums.SessionStatus;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsClassSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing class sessions.
 * Read-only access for authenticated users, CRUD for admins.
 */
@RestController
@RequestMapping("/api/v1/lms/sessions")
@RequiredArgsConstructor
@Tag(name = "LMS - Class Sessions", description = "Class session management")
@SecurityRequirement(name = "bearerAuth")
public class LmsClassSessionController {

    private final LmsClassSessionService sessionService;

    // ==================== USER ENDPOINTS (Read-only) ====================

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming sessions")
    public ResponseEntity<Page<LmsClassSessionResponseDto>> getUpcoming(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sessionService.getUpcoming(pageable));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get sessions by date")
    public ResponseEntity<List<LmsClassSessionResponseDto>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(sessionService.getByDate(date));
    }

    // ==================== ADMIN ENDPOINTS ====================

    @PostMapping
    @AdminOnly
    @Operation(summary = "Create a new class session (Admin)")
    public ResponseEntity<LmsClassSessionResponseDto> create(
            @Valid @RequestBody LmsClassSessionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.create(request));
    }

    @GetMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Get session by ID (Admin)")
    public ResponseEntity<LmsClassSessionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update a session (Admin)")
    public ResponseEntity<LmsClassSessionResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody LmsClassSessionRequestDto request) {
        return ResponseEntity.ok(sessionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete a session (Admin)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all sessions (Admin)")
    public ResponseEntity<Page<LmsClassSessionResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sessionService.getAll(pageable));
    }

    @GetMapping("/range")
    @AdminOnly
    @Operation(summary = "Get sessions by date range (Admin)")
    public ResponseEntity<List<LmsClassSessionResponseDto>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(sessionService.getByDateRange(startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    @AdminOnly
    @Operation(summary = "Update session status (Admin)")
    public ResponseEntity<LmsClassSessionResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam SessionStatus status,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(sessionService.updateStatus(id, status, reason));
    }

    @GetMapping("/{id}/attendance")
    @AdminOnly
    @Operation(summary = "Get session with attendance records (Admin)")
    public ResponseEntity<LmsClassSessionResponseDto> getSessionWithAttendance(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.getSessionWithAttendance(id));
    }
}
