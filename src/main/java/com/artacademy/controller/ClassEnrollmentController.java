package com.artacademy.controller;

import com.artacademy.dto.request.ClassEnrollmentRequestDto;
import com.artacademy.dto.request.EnrollmentStatusUpdateDto;
import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.User;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.ClassEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Class Enrollments", description = "Endpoints for managing class enrollments")
public class ClassEnrollmentController {

    private final ClassEnrollmentService enrollmentService;

    // =====================
    // Student Endpoints
    // =====================

    @PostMapping
    @Operation(summary = "Enroll in a class", description = "Create a new enrollment request (status: PENDING)")
    public ResponseEntity<ClassEnrollmentResponseDto> enroll(
            @Valid @RequestBody ClassEnrollmentRequestDto request,
            @AuthenticationPrincipal User user) {
        log.info("Enrollment request from user: {}", user.getEmail());
        return new ResponseEntity<>(enrollmentService.enroll(request, user), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @Operation(summary = "Get my enrollments", description = "Get all enrollments for the logged-in user")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getMyEnrollments(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(user, pageable));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel enrollment", description = "Cancel a pending enrollment")
    public ResponseEntity<ClassEnrollmentResponseDto> cancelEnrollment(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(enrollmentService.cancelEnrollment(id, user));
    }

    // =====================
    // Admin Endpoints
    // =====================

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all enrollments (Admin)", description = "Get all enrollments with pagination")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getAllEnrollments(Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments(pageable));
    }

    @GetMapping("/pending")
    @AdminOnly
    @Operation(summary = "Get pending enrollments (Admin)", description = "Get all pending enrollment requests")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getPendingEnrollments(Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getPendingEnrollments(pageable));
    }

    @PatchMapping("/{id}/status")
    @AdminOnly
    @Operation(summary = "Update enrollment status (Admin)", description = "Approve or reject an enrollment")
    public ResponseEntity<ClassEnrollmentResponseDto> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody EnrollmentStatusUpdateDto request) {
        log.info("Updating enrollment {} status to {}", id, request.getStatus());
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete enrollment (Admin)", description = "Permanently delete an enrollment record")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable UUID id) {
        log.warn("Deleting enrollment: {}", id);
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // Common Endpoints
    // =====================

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Get enrollment details by ID")
    public ResponseEntity<ClassEnrollmentResponseDto> getEnrollmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @GetMapping("/pending/count")
    @AdminOnly
    @Operation(summary = "Count pending enrollments (Admin)", description = "Get count of pending enrollments")
    public ResponseEntity<Long> countPendingEnrollments() {
        return ResponseEntity.ok(enrollmentService.countPendingEnrollments());
    }
}
