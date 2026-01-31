package com.artacademy.controller;

import com.artacademy.dto.request.ClassEnrollmentRequestDto;
import com.artacademy.dto.request.EnrollmentStatusUpdateDto;
import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.User;
import com.artacademy.service.ClassEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Class Enrollments", description = "Operations for enrolling in art classes")
@SecurityRequirement(name = "bearerAuth")
public class ClassEnrollmentController {

    private final ClassEnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll in a class")
    public ResponseEntity<ClassEnrollmentResponseDto> enroll(
            @Valid @RequestBody ClassEnrollmentRequestDto request,
            @AuthenticationPrincipal User user) {
        log.info("User {} requesting enrollment in class {}", user.getEmail(), request.getClassId());
        return new ResponseEntity<>(enrollmentService.enroll(request, user), HttpStatus.CREATED);
    }

    @GetMapping("/my-enrollments")
    @Operation(summary = "Get current user's enrollments")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getMyEnrollments(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(user, pageable));
    }

    @PutMapping("/{enrollmentId}/cancel")
    @Operation(summary = "Cancel an enrollment (Student)")
    public ResponseEntity<ClassEnrollmentResponseDto> cancelEnrollment(
            @PathVariable String enrollmentId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(enrollmentService.cancelEnrollment(enrollmentId, user));
    }

    // Admin endpoints

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get pending enrollments (Admin)")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getPendingEnrollments(Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getPendingEnrollments(pageable));
    }

    @GetMapping("/stats/pending-count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get count of pending enrollments (Admin)")
    public ResponseEntity<Long> countPendingEnrollments() {
        return ResponseEntity.ok(enrollmentService.countPendingEnrollments());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get all enrollments (Admin)")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getAllEnrollments(Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments(pageable));
    }

    @GetMapping("/{enrollmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get enrollment by ID (Admin)")
    public ResponseEntity<ClassEnrollmentResponseDto> getEnrollmentById(@PathVariable String enrollmentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(enrollmentId));
    }

    @PutMapping("/{enrollmentId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update enrollment status (Admin)")
    public ResponseEntity<ClassEnrollmentResponseDto> updateStatus(
            @PathVariable String enrollmentId,
            @Valid @RequestBody EnrollmentStatusUpdateDto request) {
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(enrollmentId, request));
    }

    @DeleteMapping("/{enrollmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete enrollment (Admin)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEnrollment(@PathVariable String enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
    }
}
