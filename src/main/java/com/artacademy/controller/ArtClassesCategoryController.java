package com.artacademy.controller;

import com.artacademy.dto.request.ArtClassesCategoryRequestDto;
import com.artacademy.dto.response.ArtClassesCategoryResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtClassesCategoryService;
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
@RequestMapping("/api/v1/art-classes-categories")
@RequiredArgsConstructor
@Slf4j
public class ArtClassesCategoryController {

    private final ArtClassesCategoryService categoryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtClassesCategoryResponseDto> create(
            @Valid @RequestBody ArtClassesCategoryRequestDto request) {
        log.info("Creating art classes category: {}", request.getName());
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtClassesCategoryResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtClassesCategoryResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @GetMapping("/roots")
    @PublicEndpoint
    public ResponseEntity<List<ArtClassesCategoryResponseDto>> getAllRootCategories() {
        return ResponseEntity.ok(categoryService.getAllRootCategories());
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtClassesCategoryResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtClassesCategoryRequestDto request) {
        log.info("Updating art classes category ID: {}", id);
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.warn("Deleting art classes category ID: {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
