package com.artacademy.controller;

import com.artacademy.dto.request.LmsGalleryItemRequestDto;
import com.artacademy.dto.request.LmsGalleryVerifyRequestDto;
import com.artacademy.dto.response.LmsGalleryItemResponseDto;
import com.artacademy.enums.VerificationStatus;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsGalleryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for gallery management.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/v1/lms/gallery")
@RequiredArgsConstructor
@Tag(name = "LMS - Gallery", description = "Gallery management with verification (Admin only)")
@AdminOnly
public class LmsGalleryController {

    private final LmsGalleryService galleryService;

    @PostMapping
    @Operation(summary = "Upload a new gallery item")
    public ResponseEntity<LmsGalleryItemResponseDto> create(@Valid @RequestBody LmsGalleryItemRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gallery item by ID")
    public ResponseEntity<LmsGalleryItemResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(galleryService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a gallery item")
    public ResponseEntity<LmsGalleryItemResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody LmsGalleryItemRequestDto request) {
        return ResponseEntity.ok(galleryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a gallery item")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        galleryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all gallery items (paginated)")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(galleryService.getAll(pageable));
    }

    @GetMapping("/uploader/{userId}")
    @Operation(summary = "Get gallery items by uploader")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getByUploader(
            @PathVariable String userId, Pageable pageable) {
        return ResponseEntity.ok(galleryService.getByUploader(userId, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get gallery items by verification status")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getByStatus(
            @PathVariable VerificationStatus status, Pageable pageable) {
        return ResponseEntity.ok(galleryService.getByStatus(status, pageable));
    }

    @GetMapping("/public")
    @Operation(summary = "Get public gallery items")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getPublicItems(Pageable pageable) {
        return ResponseEntity.ok(galleryService.getPublicItems(pageable));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured gallery items")
    public ResponseEntity<Page<LmsGalleryItemResponseDto>> getFeaturedItems(Pageable pageable) {
        return ResponseEntity.ok(galleryService.getFeaturedItems(pageable));
    }

    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify a gallery item")
    public ResponseEntity<LmsGalleryItemResponseDto> verify(
            @PathVariable String id,
            @Valid @RequestBody LmsGalleryVerifyRequestDto request) {
        return ResponseEntity.ok(galleryService.verify(id, request));
    }

    @GetMapping("/pending/count")
    @Operation(summary = "Count pending items for verification")
    public ResponseEntity<Long> countPending() {
        return ResponseEntity.ok(galleryService.countPending());
    }
}
