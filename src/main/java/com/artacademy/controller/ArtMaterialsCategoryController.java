package com.artacademy.controller;

import com.artacademy.dto.request.ArtMaterialsCategoryRequestDto;
import com.artacademy.dto.response.ArtMaterialsCategoryResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtMaterialsCategoryService;
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
@RequestMapping("/api/v1/art-materials-categories")
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsCategoryController {

    private final ArtMaterialsCategoryService categoryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtMaterialsCategoryResponseDto> create(
            @Valid @RequestBody ArtMaterialsCategoryRequestDto request) {
        log.info("Creating art materials category: {}", request.getName());
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtMaterialsCategoryResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtMaterialsCategoryResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @GetMapping("/roots")
    @PublicEndpoint
    public ResponseEntity<List<ArtMaterialsCategoryResponseDto>> getAllRootCategories() {
        return ResponseEntity.ok(categoryService.getAllRootCategories());
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtMaterialsCategoryResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtMaterialsCategoryRequestDto request) {
        log.info("Updating art materials category ID: {}", id);
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.warn("Deleting art materials category ID: {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
