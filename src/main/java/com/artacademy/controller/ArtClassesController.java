package com.artacademy.controller;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.service.ArtClassesService;
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
@RequestMapping("/api/v1/art-classes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Art Classes", description = "Endpoints for managing art classes")
public class ArtClassesController {

    private final ArtClassesService artClassesService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Create ArtClass")
    public ResponseEntity<ArtClassesResponseDto> create(@Valid @RequestBody ArtClassesRequestDto request) {
        return new ResponseEntity<>(artClassesService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ArtClass by ID")
    public ResponseEntity<ArtClassesResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artClassesService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all ArtClasses")
    public ResponseEntity<Page<ArtClassesResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artClassesService.getAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update ArtClass")
    public ResponseEntity<ArtClassesResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtClassesRequestDto request) {
        return ResponseEntity.ok(artClassesService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ArtClass")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        artClassesService.delete(id);
    }
}
