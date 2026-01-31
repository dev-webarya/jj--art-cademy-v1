package com.artacademy.controller;

import com.artacademy.dto.request.LmsSubscriptionRequestDto;
import com.artacademy.dto.response.LmsSubscriptionResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing student subscriptions.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/v1/lms/subscriptions")
@RequiredArgsConstructor
@Tag(name = "LMS - Subscriptions", description = "Student subscription management (Admin only)")
@AdminOnly
public class LmsSubscriptionController {

    private final LmsSubscriptionService subscriptionService;

    @PostMapping
    @Operation(summary = "Create a new subscription for a student")
    public ResponseEntity<LmsSubscriptionResponseDto> create(@Valid @RequestBody LmsSubscriptionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by ID")
    public ResponseEntity<LmsSubscriptionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a subscription")
    public ResponseEntity<LmsSubscriptionResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody LmsSubscriptionRequestDto request) {
        return ResponseEntity.ok(subscriptionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subscription")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all subscriptions (paginated)")
    public ResponseEntity<Page<LmsSubscriptionResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(subscriptionService.getAll(pageable));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active subscriptions (paginated)")
    public ResponseEntity<Page<LmsSubscriptionResponseDto>> getActive(Pageable pageable) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptions(pageable));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all subscriptions for a student")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(subscriptionService.getByStudentId(studentId));
    }

    @GetMapping("/student/{studentId}/active")
    @Operation(summary = "Get active subscription for a student")
    public ResponseEntity<LmsSubscriptionResponseDto> getActiveByStudent(@PathVariable String studentId) {
        LmsSubscriptionResponseDto subscription = subscriptionService.getActiveByStudentId(studentId);
        return subscription != null ? ResponseEntity.ok(subscription) : ResponseEntity.notFound().build();
    }

    @GetMapping("/month/{year}/{month}")
    @Operation(summary = "Get all subscriptions for a specific month")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getByMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        return ResponseEntity.ok(subscriptionService.getByMonth(month, year));
    }

    @PostMapping("/student/{studentId}/renew")
    @Operation(summary = "Renew subscription for next month")
    public ResponseEntity<LmsSubscriptionResponseDto> renew(@PathVariable String studentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.renewSubscription(studentId));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a subscription")
    public ResponseEntity<LmsSubscriptionResponseDto> cancel(@PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(id));
    }

    @GetMapping("/over-limit")
    @Operation(summary = "Get students who have exceeded their session limit")
    public ResponseEntity<List<LmsSubscriptionResponseDto>> getOverLimit() {
        return ResponseEntity.ok(subscriptionService.getOverLimitSubscriptions());
    }
}
