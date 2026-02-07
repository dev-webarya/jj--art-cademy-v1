package com.artacademy.controller;

import com.artacademy.dto.request.LmsGalleryItemRequestDto;
import com.artacademy.dto.request.LmsGalleryVerifyRequestDto;
import com.artacademy.dto.response.LmsGalleryItemResponseDto;
import com.artacademy.entity.User;
import com.artacademy.enums.VerificationStatus;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.LmsGalleryService;
import io.swagger.v3.oas.annotations.Operation;
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

/**
 * REST Controller for gallery management.
 * Public access for viewing, authenticated for upload, admin for verification.
 */
@RestController
@RequestMapping("/api/v1/lms/gallery")
@RequiredArgsConstructor
@Tag(name = "LMS - Gallery", description = "Gallery management with verification")
@SecurityRequirement(name = "bearerAuth")
public class LmsGalleryController {

    private final LmsGalleryService galleryService;

    // ==================== PUBLIC ENDPOINTS ====================

    @GetMapping("/public")
    @PublicEndpoint
    @Operation(summary = "Get public gallery items")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getPublicItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(galleryService.getPublicItems(pageable));
    }

    @GetMapping("/featured")
    @PublicEndpoint
    @Operation(summary = "Get featured gallery items")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getFeaturedItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(galleryService.getFeaturedItems(pageable));
    }

    // ==================== USER ENDPOINTS ====================

    @PostMapping
    @Operation(summary = "Upload a new gallery item (auto PENDING status)")
    public ResponseEntity<LmsGalleryItemResponseDto> create(
            @Valid @RequestBody LmsGalleryItemRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.create(request));
    }

    @GetMapping("/my")
    @Operation(summary = "Get current user's gallery uploads")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getMyUploads(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(galleryService.getByUploader(user.getId(), pageable));
    }

    // ==================== ADMIN ENDPOINTS ====================

    @GetMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Get gallery item by ID (Admin)")
    public ResponseEntity<LmsGalleryItemResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(galleryService.getById(id));
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update a gallery item (Admin)")
    public ResponseEntity<LmsGalleryItemResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody LmsGalleryItemRequestDto request) {
        return ResponseEntity.ok(galleryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete a gallery item (Admin)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        galleryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all gallery items (Admin)")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(galleryService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    @AdminOnly
    @Operation(summary = "Get gallery items by status (Admin)")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getByStatus(
            @PathVariable VerificationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(galleryService.getByStatus(status, pageable));
    }

    @PostMapping("/{id}/verify")
    @AdminOnly
    @Operation(summary = "Verify a gallery item (Admin)")
    public ResponseEntity<LmsGalleryItemResponseDto> verify(
            @PathVariable String id,
            @Valid @RequestBody LmsGalleryVerifyRequestDto request) {
        return ResponseEntity.ok(galleryService.verify(id, request));
    }

    @GetMapping("/pending/count")
    @AdminOnly
    @Operation(summary = "Count pending items (Admin)")
    public ResponseEntity<Long> countPending() {
        return ResponseEntity.ok(galleryService.countPending());
    }
}
