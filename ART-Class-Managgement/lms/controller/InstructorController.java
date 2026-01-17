package com.artacademy.lms.controller;

import com.artacademy.entity.User;
import com.artacademy.lms.dto.request.InstructorRequestDto;
import com.artacademy.lms.dto.response.InstructorResponseDto;
import com.artacademy.lms.service.InstructorService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lms/instructors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "LMS - Instructors", description = "Instructor management endpoints")
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    @AdminOnly
    @Operation(summary = "Create instructor (Admin)", description = "Create instructor profile for a user")
    public ResponseEntity<InstructorResponseDto> create(@Valid @RequestBody InstructorRequestDto request) {
        return new ResponseEntity<>(instructorService.createInstructor(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update instructor (Admin)")
    public ResponseEntity<InstructorResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody InstructorRequestDto request) {
        return ResponseEntity.ok(instructorService.updateInstructor(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete instructor (Admin)", description = "Soft deletes instructor and unassigns from all batches")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.warn("Admin deleting instructor: {}", id);
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get instructor by ID")
    public ResponseEntity<InstructorResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(instructorService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all instructors")
    public ResponseEntity<Page<InstructorResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(instructorService.getAll(pageable));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active instructors")
    public ResponseEntity<Page<InstructorResponseDto>> getActive(Pageable pageable) {
        return ResponseEntity.ok(instructorService.getActiveInstructors(pageable));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my instructor profile")
    public ResponseEntity<InstructorResponseDto> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(instructorService.getMyProfile(user));
    }

    @GetMapping("/batches-needing-instructor/count")
    @AdminOnly
    @Operation(summary = "Count batches needing instructor (Admin)")
    public ResponseEntity<Long> countBatchesNeedingInstructor() {
        return ResponseEntity.ok(instructorService.countBatchesNeedingInstructor());
    }
}
