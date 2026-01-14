package com.artacademy.controller;

import com.artacademy.dto.request.ArtWorksCategoryRequestDto;
import com.artacademy.dto.response.ArtWorksCategoryResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtWorksCategoryService;
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
@RequestMapping("/api/v1/artworks-categories")
@RequiredArgsConstructor
@Slf4j
public class ArtWorksCategoryController {

    private final ArtWorksCategoryService categoryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtWorksCategoryResponseDto> create(@Valid @RequestBody ArtWorksCategoryRequestDto request) {
        log.info("Creating artwork category: {}", request.getName());
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtWorksCategoryResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtWorksCategoryResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @GetMapping("/roots")
    @PublicEndpoint
    public ResponseEntity<List<ArtWorksCategoryResponseDto>> getAllRootCategories() {
        return ResponseEntity.ok(categoryService.getAllRootCategories());
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtWorksCategoryResponseDto> update(@PathVariable UUID id,
            @Valid @RequestBody ArtWorksCategoryRequestDto request) {
        log.info("Updating artwork category ID: {}", id);
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.warn("Deleting artwork category ID: {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
