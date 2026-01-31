package com.artacademy.controller;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import com.artacademy.service.ArtWorksService;
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
@RequestMapping("/api/v1/art-works")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Art Works", description = "Endpoints for managing art works")
public class ArtWorksController {

    private final ArtWorksService artWorksService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Create ArtWork")
    public ResponseEntity<ArtWorksResponseDto> create(@Valid @RequestBody ArtWorksRequestDto request) {
        log.info("Request to create artwork: {}", request.getName());
        return new ResponseEntity<>(artWorksService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ArtWork by ID")
    public ResponseEntity<ArtWorksResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artWorksService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all ArtWorks")
    public ResponseEntity<Page<ArtWorksResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artWorksService.getAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update ArtWork")
    public ResponseEntity<ArtWorksResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtWorksRequestDto request) {
        log.info("Request to update artwork: {}", id);
        return ResponseEntity.ok(artWorksService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ArtWork")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        log.warn("Request to delete artwork: {}", id);
        artWorksService.delete(id);
    }

    @PostMapping("/{id}/views")
    @Operation(summary = "Increment view count")
    public ResponseEntity<ArtWorksResponseDto> incrementViews(@PathVariable String id) {
        return ResponseEntity.ok(artWorksService.incrementViews(id));
    }

    @PostMapping("/{id}/likes")
    @Operation(summary = "Increment like count")
    public ResponseEntity<ArtWorksResponseDto> incrementLikes(@PathVariable String id) {
        return ResponseEntity.ok(artWorksService.incrementLikes(id));
    }
}
