package com.artacademy.controller;

import com.artacademy.dto.request.ArtGalleryCategoryRequestDto;
import com.artacademy.dto.response.ArtGalleryCategoryResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtGalleryCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gallery-categories")
@RequiredArgsConstructor
@Slf4j
public class ArtGalleryCategoryController {

    private final ArtGalleryCategoryService categoryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtGalleryCategoryResponseDto> create(
            @Valid @RequestBody ArtGalleryCategoryRequestDto request) {
        log.info("Creating gallery category: {}", request.getName());
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtGalleryCategoryResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtGalleryCategoryResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @GetMapping("/roots")
    @PublicEndpoint
    public ResponseEntity<List<ArtGalleryCategoryResponseDto>> getAllRootCategories() {
        return ResponseEntity.ok(categoryService.getAllRootCategories());
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtGalleryCategoryResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtGalleryCategoryRequestDto request) {
        log.info("Updating gallery category ID: {}", id);
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.warn("Deleting gallery category ID: {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
