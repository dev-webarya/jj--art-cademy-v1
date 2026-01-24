package com.artacademy.lms.controller;

import com.artacademy.entity.User;
import com.artacademy.lms.dto.request.BatchRequestDto;
import com.artacademy.lms.dto.response.BatchResponseDto;
import com.artacademy.lms.dto.response.BatchStudentResponseDto;
import com.artacademy.lms.service.BatchService;
import com.artacademy.security.annotations.AdminOnly;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lms/batches")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "LMS - Batches", description = "Batch management endpoints")
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    @AdminOnly
    @Operation(summary = "Create batch (Admin)")
    public ResponseEntity<BatchResponseDto> create(@Valid @RequestBody BatchRequestDto request) {
        return new ResponseEntity<>(batchService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update batch (Admin)")
    public ResponseEntity<BatchResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody BatchRequestDto request) {
        return ResponseEntity.ok(batchService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete batch (Admin)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        batchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get batch by ID")
    public ResponseEntity<BatchResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(batchService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all batches")
    public ResponseEntity<Page<BatchResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(batchService.getAll(pageable));
    }

    @PatchMapping("/{id}/assign-instructor/{instructorId}")
    @AdminOnly
    @Operation(summary = "Assign instructor to batch (Admin)")
    public ResponseEntity<BatchResponseDto> assignInstructor(
            @PathVariable UUID id,
            @PathVariable UUID instructorId) {
        return ResponseEntity.ok(batchService.assignInstructor(id, instructorId));
    }

    @GetMapping("/needing-instructor")
    @AdminOnly
    @Operation(summary = "Get batches needing instructor (Admin)")
    public ResponseEntity<Page<BatchResponseDto>> getBatchesNeedingInstructor(Pageable pageable) {
        return ResponseEntity.ok(batchService.getBatchesNeedingInstructor(pageable));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my batches (Instructor)")
    public ResponseEntity<Page<BatchResponseDto>> getMyBatches(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(batchService.getMyBatches(user, pageable));
    }

    @GetMapping("/enrolled")
    @Operation(summary = "Get enrolled batches (Student)")
    public ResponseEntity<Page<BatchResponseDto>> getStudentBatches(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(batchService.getStudentBatches(user, pageable));
    }

    // Student management
    @PostMapping("/{id}/students")
    @AdminOnly
    @Operation(summary = "Add student to batch (Admin)")
    public ResponseEntity<BatchStudentResponseDto> addStudent(
            @PathVariable UUID id,
            @RequestParam UUID userId,
            @RequestParam(required = false) UUID enrollmentId) {
        return new ResponseEntity<>(batchService.addStudent(id, userId, enrollmentId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/students/{userId}")
    @AdminOnly
    @Operation(summary = "Remove student from batch (Admin)")
    public ResponseEntity<Void> removeStudent(
            @PathVariable UUID id,
            @PathVariable UUID userId) {
        batchService.removeStudent(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/students")
    @Operation(summary = "Get students in batch")
    public ResponseEntity<List<BatchStudentResponseDto>> getStudents(@PathVariable UUID id) {
        return ResponseEntity.ok(batchService.getStudents(id));
    }
}
