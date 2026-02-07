package com.artacademy.controller;

import com.artacademy.dto.request.LmsAttendanceRequestDto;
import com.artacademy.dto.response.EligibleStudentDto;
import com.artacademy.dto.response.LmsAttendanceResponseDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.entity.User;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for attendance management.
 * User endpoints for viewing own attendance, admin endpoints for management.
 */
@RestController
@RequestMapping("/api/v1/lms/attendance")
@RequiredArgsConstructor
@Tag(name = "LMS - Attendance", description = "Attendance management")
@SecurityRequirement(name = "bearerAuth")
public class LmsAttendanceController {

    private final LmsAttendanceService attendanceService;

    // ==================== USER ENDPOINTS ====================

    @GetMapping("/my")
    @Operation(summary = "Get current user's attendance history")
    public ResponseEntity<Page<LmsAttendanceResponseDto>> getMyAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(attendanceService.getMyAttendance(pageable));
    }

    // ==================== ADMIN ENDPOINTS ====================

    @GetMapping("/eligible-students")
    @AdminOnly
    @Operation(summary = "Get students eligible for attendance (Admin)")
    public ResponseEntity<List<EligibleStudentDto>> getEligibleStudents() {
        return ResponseEntity.ok(attendanceService.getEligibleStudentsForAttendance());
    }

    @PostMapping
    @AdminOnly
    @Operation(summary = "Mark attendance for a session (Admin)")
    public ResponseEntity<LmsClassSessionResponseDto> markAttendance(
            @Valid @RequestBody LmsAttendanceRequestDto request) {
        return ResponseEntity.ok(attendanceService.markAttendance(request));
    }

    @GetMapping("/session/{sessionId}")
    @AdminOnly
    @Operation(summary = "Get attendance records for a session (Admin)")
    public ResponseEntity<List<LmsAttendanceResponseDto>> getBySession(
            @PathVariable String sessionId) {
        return ResponseEntity.ok(attendanceService.getBySessionId(sessionId));
    }

    @GetMapping("/student/{studentId}")
    @AdminOnly
    @Operation(summary = "Get attendance history for a student (Admin)")
    public ResponseEntity<Page<LmsAttendanceResponseDto>> getByStudent(
            @PathVariable String studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(attendanceService.getByStudentId(studentId, pageable));
    }

    @GetMapping("/student/{studentId}/logs")
    @AdminOnly
    @Operation(summary = "Get full attendance logs for a student (Admin)")
    public ResponseEntity<List<LmsAttendanceResponseDto>> getStudentAttendanceLogs(
            @PathVariable String studentId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceLogs(studentId, month, year));
    }

    @GetMapping("/over-limit/{year}/{month}")
    @AdminOnly
    @Operation(summary = "Get students over session limit (Admin)")
    public ResponseEntity<List<LmsAttendanceResponseDto>> getOverLimitStudents(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        return ResponseEntity.ok(attendanceService.getOverLimitStudents(month, year));
    }
}
