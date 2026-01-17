package com.artacademy.service;

import com.artacademy.dto.request.ClassEnrollmentRequestDto;
import com.artacademy.dto.request.EnrollmentStatusUpdateDto;
import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClassEnrollmentService {

    // Student Operations
    ClassEnrollmentResponseDto enroll(ClassEnrollmentRequestDto request, User user);

    Page<ClassEnrollmentResponseDto> getMyEnrollments(User user, Pageable pageable);

    ClassEnrollmentResponseDto cancelEnrollment(String enrollmentId, User user);

    // Admin Operations
    Page<ClassEnrollmentResponseDto> getAllEnrollments(Pageable pageable);

    Page<ClassEnrollmentResponseDto> getPendingEnrollments(Pageable pageable);

    ClassEnrollmentResponseDto updateEnrollmentStatus(String enrollmentId, EnrollmentStatusUpdateDto request);

    // Common Operations
    ClassEnrollmentResponseDto getEnrollmentById(String enrollmentId);

    void deleteEnrollment(String enrollmentId);

    // Statistics
    long countPendingEnrollments();
}
