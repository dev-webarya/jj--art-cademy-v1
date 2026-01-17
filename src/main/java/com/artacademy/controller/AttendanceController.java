package com.artacademy.controller;

import com.artacademy.dto.attendance.*;
import com.artacademy.entity.User;
import com.artacademy.enums.SessionStatus;
import com.artacademy.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance Management", description = "APIs for managing class attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // ==================== SESSION ENDPOINTS ====================

    @PostMapping("/sessions")
    @Operation(summary = "Create a new attendance session")
    public ResponseEntity<AttendanceSessionDTO> createSession(@Valid @RequestBody CreateSessionRequest request) {
        AttendanceSessionDTO session = attendanceService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping("/sessions/{sessionId}")
    @Operation(summary = "Get session by ID")
    public ResponseEntity<AttendanceSessionDTO> getSessionById(@PathVariable String sessionId) {
        return ResponseEntity.ok(attendanceService.getSessionById(sessionId));
    }

    @GetMapping("/sessions/class/{classId}")
    @Operation(summary = "Get all sessions for a class")
    public ResponseEntity<List<AttendanceSessionDTO>> getSessionsByClass(@PathVariable String classId) {
        return ResponseEntity.ok(attendanceService.getSessionsByClassId(classId));
    }

    @GetMapping("/sessions/date/{date}")
    @Operation(summary = "Get all sessions for a specific date")
    public ResponseEntity<List<AttendanceSessionDTO>> getSessionsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getSessionsByDate(date));
    }

    @GetMapping("/sessions/range")
    @Operation(summary = "Get sessions within a date range")
    public ResponseEntity<List<AttendanceSessionDTO>> getSessionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getSessionsByDateRange(startDate, endDate));
    }

    @PatchMapping("/sessions/{sessionId}/status")
    @Operation(summary = "Update session status")
    public ResponseEntity<AttendanceSessionDTO> updateSessionStatus(
            @PathVariable String sessionId,
            @RequestParam SessionStatus status) {
        return ResponseEntity.ok(attendanceService.updateSessionStatus(sessionId, status));
    }

    // ==================== ATTENDANCE MARKING ENDPOINTS ====================

    @PostMapping("/mark")
    @Operation(summary = "Mark attendance for a single student")
    public ResponseEntity<AttendanceRecordDTO> markAttendance(
            @Valid @RequestBody MarkAttendanceRequest request,
            @AuthenticationPrincipal User currentUser) {
        AttendanceRecordDTO record = attendanceService.markAttendance(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PostMapping("/bulk-mark")
    @Operation(summary = "Mark attendance for multiple students at once")
    public ResponseEntity<List<AttendanceRecordDTO>> bulkMarkAttendance(
            @Valid @RequestBody BulkMarkAttendanceRequest request,
            @AuthenticationPrincipal User currentUser) {
        List<AttendanceRecordDTO> records = attendanceService.bulkMarkAttendance(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(records);
    }

    // ==================== ATTENDANCE QUERY ENDPOINTS ====================

    @GetMapping("/session/{sessionId}")
    @Operation(summary = "Get all attendance records for a session")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceBySession(@PathVariable String sessionId) {
        return ResponseEntity.ok(attendanceService.getAttendanceBySession(sessionId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get attendance history for a student")
    public ResponseEntity<List<AttendanceRecordDTO>> getStudentAttendanceHistory(@PathVariable String studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceHistory(studentId));
    }

    @GetMapping("/student/{studentId}/range")
    @Operation(summary = "Get student attendance within a date range")
    public ResponseEntity<List<AttendanceRecordDTO>> getStudentAttendanceByDateRange(
            @PathVariable String studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceByDateRange(studentId, startDate, endDate));
    }

    // ==================== REPORT ENDPOINTS ====================

    @GetMapping("/report/student/{studentId}")
    @Operation(summary = "Get attendance report for a student")
    public ResponseEntity<AttendanceReportDTO> getStudentAttendanceReport(
            @PathVariable String studentId,
            @RequestParam(required = false) String classId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceReport(studentId, classId));
    }

    @GetMapping("/report/class/{classId}")
    @Operation(summary = "Get attendance report for a class (all enrolled students)")
    public ResponseEntity<List<AttendanceReportDTO>> getClassAttendanceReport(@PathVariable String classId) {
        return ResponseEntity.ok(attendanceService.getClassAttendanceReport(classId));
    }

    // ==================== ENROLLMENT ENDPOINTS ====================

    @PostMapping("/enrollments")
    @Operation(summary = "Enroll a student in a class")
    public ResponseEntity<ClassEnrollmentDTO> enrollStudent(@Valid @RequestBody EnrollStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.enrollStudent(request));
    }

    @GetMapping("/enrollments/class/{classId}")
    @Operation(summary = "Get all enrollments for a class")
    public ResponseEntity<List<ClassEnrollmentDTO>> getEnrollmentsByClass(@PathVariable String classId) {
        return ResponseEntity.ok(attendanceService.getEnrollmentsByClassId(classId));
    }

    @GetMapping("/enrollments/student/{studentId}")
    @Operation(summary = "Get all class enrollments for a student")
    public ResponseEntity<List<ClassEnrollmentDTO>> getEnrollmentsByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(attendanceService.getEnrollmentsByStudentId(studentId));
    }
}
