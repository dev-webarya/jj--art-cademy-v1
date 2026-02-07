package com.artacademy.controller;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.enums.VerificationStatus;
import com.artacademy.service.ArtGalleryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/art-galleries")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Art Galleries", description = "Endpoints for managing art galleries")
public class ArtGalleryController {

    private final ArtGalleryService artGalleryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Create ArtGallery")
    public ResponseEntity<ArtGalleryResponseDto> create(@Valid @RequestBody ArtGalleryRequestDto request) {
        return new ResponseEntity<>(artGalleryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ArtGallery by ID")
    public ResponseEntity<ArtGalleryResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artGalleryService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all ArtGalleries")
    public ResponseEntity<Page<ArtGalleryResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artGalleryService.getAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update ArtGallery")
    public ResponseEntity<ArtGalleryResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtGalleryRequestDto request) {
        return ResponseEntity.ok(artGalleryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ArtGallery")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        artGalleryService.delete(id);
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Verify ArtGallery (Approve/Reject)")
    public ResponseEntity<ArtGalleryResponseDto> verify(@PathVariable String id,
            @RequestParam VerificationStatus status) {
        return ResponseEntity.ok(artGalleryService.verifyGallery(id, status));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get user's own galleries with optional status filter")
    public ResponseEntity<Page<ArtGalleryResponseDto>> getMyGalleries(
            @RequestParam(required = false) VerificationStatus status,
            Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(artGalleryService.getMyGalleriesByStatus(status, pageable));
        }
        return ResponseEntity.ok(artGalleryService.getMyGalleries(pageable));
    }
}
