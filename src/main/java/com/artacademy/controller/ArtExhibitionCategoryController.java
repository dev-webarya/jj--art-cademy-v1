package com.artacademy.controller;

import com.artacademy.dto.request.ArtExhibitionCategoryRequestDto;
import com.artacademy.dto.response.ArtExhibitionCategoryResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtExhibitionCategoryService;
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
@RequestMapping("/api/v1/exhibition-categories")
@RequiredArgsConstructor
@Slf4j
public class ArtExhibitionCategoryController {

    private final ArtExhibitionCategoryService categoryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtExhibitionCategoryResponseDto> create(
            @Valid @RequestBody ArtExhibitionCategoryRequestDto request) {
        log.info("Creating exhibition category: {}", request.getName());
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtExhibitionCategoryResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtExhibitionCategoryResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @GetMapping("/roots")
    @PublicEndpoint
    public ResponseEntity<List<ArtExhibitionCategoryResponseDto>> getAllRootCategories() {
        return ResponseEntity.ok(categoryService.getAllRootCategories());
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtExhibitionCategoryResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtExhibitionCategoryRequestDto request) {
        log.info("Updating exhibition category ID: {}", id);
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.warn("Deleting exhibition category ID: {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
