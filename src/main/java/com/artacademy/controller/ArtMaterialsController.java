package com.artacademy.controller;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.service.ArtMaterialsService;
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
@RequestMapping("/api/v1/art-materials")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Art Materials", description = "Endpoints for managing art materials")
public class ArtMaterialsController {

    private final ArtMaterialsService artMaterialsService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Create ArtMaterial")
    public ResponseEntity<ArtMaterialsResponseDto> create(@Valid @RequestBody ArtMaterialsRequestDto request) {
        log.info("Request to create material: {}", request.getName());
        return new ResponseEntity<>(artMaterialsService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ArtMaterial by ID")
    public ResponseEntity<ArtMaterialsResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artMaterialsService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all ArtMaterials")
    public ResponseEntity<Page<ArtMaterialsResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artMaterialsService.getAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update ArtMaterial")
    public ResponseEntity<ArtMaterialsResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtMaterialsRequestDto request) {
        log.info("Request to update material: {}", id);
        return ResponseEntity.ok(artMaterialsService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ArtMaterial")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        log.warn("Request to delete material: {}", id);
        artMaterialsService.delete(id);
    }
}
