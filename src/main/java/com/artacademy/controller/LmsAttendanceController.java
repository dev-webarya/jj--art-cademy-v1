package com.artacademy.controller;

import com.artacademy.dto.request.LmsAttendanceRequestDto;
import com.artacademy.dto.response.LmsAttendanceResponseDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for attendance management.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/v1/lms/attendance")
@RequiredArgsConstructor
@Tag(name = "LMS - Attendance", description = "Attendance management (Admin only)")
@AdminOnly
public class LmsAttendanceController {

    private final LmsAttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Mark attendance for a session")
    public ResponseEntity<LmsClassSessionResponseDto> markAttendance(
            @Valid @RequestBody LmsAttendanceRequestDto request) {
        return ResponseEntity.ok(attendanceService.markAttendance(request));
    }

    @GetMapping("/session/{sessionId}")
    @Operation(summary = "Get attendance records for a session")
    public ResponseEntity<List<LmsAttendanceResponseDto>> getBySession(@PathVariable String sessionId) {
        return ResponseEntity.ok(attendanceService.getBySessionId(sessionId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get attendance history for a student")
    public ResponseEntity<Page<LmsAttendanceResponseDto>> getByStudent(
            @PathVariable String studentId, Pageable pageable) {
        return ResponseEntity.ok(attendanceService.getByStudentId(studentId, pageable));
    }

    @GetMapping("/student/{studentId}/monthly/{year}/{month}")
    @Operation(summary = "Get monthly attendance summary for a student")
    public ResponseEntity<LmsAttendanceResponseDto> getStudentMonthlyAttendance(
            @PathVariable String studentId,
            @PathVariable Integer year,
            @PathVariable Integer month) {
        LmsAttendanceResponseDto response = attendanceService.getStudentMonthlyAttendance(studentId, month, year);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @GetMapping("/over-limit/{year}/{month}")
    @Operation(summary = "Get students over session limit for a month")
    public ResponseEntity<List<LmsAttendanceResponseDto>> getOverLimitStudents(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        return ResponseEntity.ok(attendanceService.getOverLimitStudents(month, year));
    }
}
