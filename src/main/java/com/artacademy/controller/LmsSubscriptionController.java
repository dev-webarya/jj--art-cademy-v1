package com.artacademy.controller;

import com.artacademy.dto.request.LmsSubscriptionRequestDto;
import com.artacademy.dto.response.LmsSubscriptionResponseDto;
import com.artacademy.entity.User;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing student subscriptions.
 * Admin endpoints for CRUD, user endpoints for viewing own subscriptions.
 */
@RestController
@RequestMapping("/api/v1/lms/subscriptions")
@RequiredArgsConstructor
@Tag(name = "LMS - Subscriptions", description = "Monthly subscription management")
@SecurityRequirement(name = "bearerAuth")
public class LmsSubscriptionController {

    private final LmsSubscriptionService subscriptionService;

    // ==================== USER ENDPOINTS ====================

    @GetMapping("/my")
    @Operation(summary = "Get current user's subscriptions")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getMySubscriptions(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(subscriptionService.getByStudentId(user.getId()));
    }

    @GetMapping("/my/active")
    @Operation(summary = "Get current user's active subscription")
    public ResponseEntity<LmsSubscriptionResponseDto> getMyActiveSubscription(
            @AuthenticationPrincipal User user) {
        LmsSubscriptionResponseDto subscription = subscriptionService.getActiveByStudentId(user.getId());
        return subscription != null ? ResponseEntity.ok(subscription) : ResponseEntity.notFound().build();
    }

    // ==================== ADMIN ENDPOINTS ====================

    @PostMapping
    @AdminOnly
    @Operation(summary = "Create a new monthly subscription (Admin)")
    public ResponseEntity<LmsSubscriptionResponseDto> create(@Valid @RequestBody LmsSubscriptionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(request));
    }

    @GetMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Get subscription by ID (Admin)")
    public ResponseEntity<LmsSubscriptionResponseDto> getById(
            @Parameter(description = "Subscription ID") @PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update a subscription (Admin)")
    public ResponseEntity<LmsSubscriptionResponseDto> update(
            @Parameter(description = "Subscription ID") @PathVariable String id,
            @Valid @RequestBody LmsSubscriptionRequestDto request) {
        return ResponseEntity.ok(subscriptionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete a subscription (Admin)")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Subscription ID") @PathVariable String id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all subscriptions (Admin)")
    public ResponseEntity<Page<LmsSubscriptionResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(subscriptionService.getAll(pageable));
    }

    @GetMapping("/active")
    @AdminOnly
    @Operation(summary = "Get all active subscriptions (Admin)")
    public ResponseEntity<Page<LmsSubscriptionResponseDto>> getActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptions(pageable));
    }

    @GetMapping("/student/{studentId}")
    @AdminOnly
    @Operation(summary = "Get subscriptions for a student (Admin)")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getByStudent(
            @PathVariable String studentId) {
        return ResponseEntity.ok(subscriptionService.getByStudentId(studentId));
    }

    @PostMapping("/enrollment/{enrollmentId}/renew")
    @AdminOnly
    @Operation(summary = "Renew subscription for next month (Admin)")
    public ResponseEntity<LmsSubscriptionResponseDto> renew(
            @PathVariable String enrollmentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.renewSubscription(enrollmentId));
    }

    @PostMapping("/{id}/cancel")
    @AdminOnly
    @Operation(summary = "Cancel a subscription (Admin)")
    public ResponseEntity<LmsSubscriptionResponseDto> cancel(
            @PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(id));
    }

    @GetMapping("/over-limit")
    @AdminOnly
    @Operation(summary = "Get students over session limit (Admin)")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getOverLimit() {
        return ResponseEntity.ok(subscriptionService.getOverLimitSubscriptions());
    }
}
