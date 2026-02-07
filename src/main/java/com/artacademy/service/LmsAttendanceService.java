package com.artacademy.service;

import com.artacademy.dto.request.LmsAttendanceRequestDto;
import com.artacademy.dto.response.EligibleStudentDto;
import com.artacademy.dto.response.LmsAttendanceResponseDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for attendance management.
 * Tracks session attendance with over-limit detection.
 */
public interface LmsAttendanceService {

    /**
     * Get list of students eligible for attendance.
     * Returns students with APPROVED enrollment AND active subscription for current
     * month.
     */
    List<EligibleStudentDto> getEligibleStudentsForAttendance();

    /**
     * Mark attendance for a session.
     * Returns updated session with attendance records including over-limit flags.
     */
    LmsClassSessionResponseDto markAttendance(LmsAttendanceRequestDto request);

    /**
     * Get all attendance records for a session.
     */
    List<LmsAttendanceResponseDto> getBySessionId(String sessionId);

    /**
     * Get attendance history for a student.
     */
    Page<LmsAttendanceResponseDto> getByStudentId(String studentId, Pageable pageable);

    /**
     * Get current user's attendance history.
     */
    Page<LmsAttendanceResponseDto> getMyAttendance(Pageable pageable);

    /**
     * Get students who are over their monthly session limit.
     */
    List<LmsAttendanceResponseDto> getOverLimitStudents(Integer month, Integer year);

    /**
     * Get monthly attendance summary for a student.
     */
    LmsAttendanceResponseDto getStudentMonthlyAttendance(String studentId, Integer month, Integer year);

    /**
     * Get full attendance logs for a student, optionally filtered by month/year.
     */
    List<LmsAttendanceResponseDto> getStudentAttendanceLogs(String studentId, Integer month, Integer year);
}
