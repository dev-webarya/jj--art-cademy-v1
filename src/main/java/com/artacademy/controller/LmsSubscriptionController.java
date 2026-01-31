package com.artacademy.controller;

import com.artacademy.dto.request.LmsSubscriptionRequestDto;
import com.artacademy.dto.response.LmsSubscriptionResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing student subscriptions.
 * Only students with APPROVED enrollment can have subscriptions.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/v1/lms/subscriptions")
@RequiredArgsConstructor
@Tag(name = "LMS - Subscriptions", description = "Monthly subscription management for approved students (Admin only)")
@AdminOnly
public class LmsSubscriptionController {

    private final LmsSubscriptionService subscriptionService;

    @PostMapping
    @Operation(summary = "Create a new monthly subscription for an approved student")
    public ResponseEntity<LmsSubscriptionResponseDto> create(@Valid @RequestBody LmsSubscriptionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by ID")
    public ResponseEntity<LmsSubscriptionResponseDto> getById(
            @Parameter(description = "Subscription ID", example = "sub123abc") @PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a subscription")
    public ResponseEntity<LmsSubscriptionResponseDto> update(
            @Parameter(description = "Subscription ID", example = "sub123abc") @PathVariable String id,
            @Valid @RequestBody LmsSubscriptionRequestDto request) {
        return ResponseEntity.ok(subscriptionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subscription")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Subscription ID", example = "sub123abc") @PathVariable String id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all subscriptions (paginated)")
    public ResponseEntity<Page<LmsSubscriptionResponseDto>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "100") @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(subscriptionService.getAll(pageable));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active subscriptions (paginated)")
    public ResponseEntity<Page<LmsSubscriptionResponseDto>> getActive(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "100") @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptions(pageable));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all subscriptions for a student (by user ID)")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getByStudent(
            @Parameter(description = "User ID", example = "user123abc") @PathVariable String studentId) {
        return ResponseEntity.ok(subscriptionService.getByStudentId(studentId));
    }

    @GetMapping("/student/{studentId}/active")
    @Operation(summary = "Get active subscription for a student")
    public ResponseEntity<LmsSubscriptionResponseDto> getActiveByStudent(
            @Parameter(description = "User ID", example = "user123abc") @PathVariable String studentId) {
        LmsSubscriptionResponseDto subscription = subscriptionService.getActiveByStudentId(studentId);
        return subscription != null ? ResponseEntity.ok(subscription) : ResponseEntity.notFound().build();
    }

    @GetMapping("/month/{year}/{month}")
    @Operation(summary = "Get all subscriptions for a specific month")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getByMonth(
            @Parameter(description = "Year", example = "2026") @PathVariable Integer year,
            @Parameter(description = "Month (1-12)", example = "2") @PathVariable Integer month) {
        return ResponseEntity.ok(subscriptionService.getByMonth(month, year));
    }

    @PostMapping("/enrollment/{enrollmentId}/renew")
    @Operation(summary = "Renew subscription for next month (only for APPROVED enrollments)")
    public ResponseEntity<LmsSubscriptionResponseDto> renew(
            @Parameter(description = "Enrollment ID", example = "enrollment123abc") @PathVariable String enrollmentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.renewSubscription(enrollmentId));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a subscription")
    public ResponseEntity<LmsSubscriptionResponseDto> cancel(
            @Parameter(description = "Subscription ID", example = "sub123abc") @PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(id));
    }

    @GetMapping("/over-limit")
    @Operation(summary = "Get students who have exceeded their session limit (8+)")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getOverLimit() {
        return ResponseEntity.ok(subscriptionService.getOverLimitSubscriptions());
    }
}
