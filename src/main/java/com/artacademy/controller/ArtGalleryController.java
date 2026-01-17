package com.artacademy.controller;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtGalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/art-galleries")
@RequiredArgsConstructor
@Slf4j
public class ArtGalleryController {

    private final ArtGalleryService artGalleryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtGalleryResponseDto> create(@Valid @RequestBody ArtGalleryRequestDto request) {
        log.info("Creating gallery: {}", request.getName());
        return new ResponseEntity<>(artGalleryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtGalleryResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artGalleryService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtGalleryResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artGalleryService.getAll(pageable));
    }

    @GetMapping("/active")
    @PublicEndpoint
    public ResponseEntity<List<ArtGalleryResponseDto>> getAllActive() {
        return ResponseEntity.ok(artGalleryService.getAllActive());
    }

    @GetMapping("/category/{categoryId}")
    @PublicEndpoint
    public ResponseEntity<List<ArtGalleryResponseDto>> getByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(artGalleryService.getByCategory(categoryId));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtGalleryResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtGalleryRequestDto request) {
        return ResponseEntity.ok(artGalleryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artGalleryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
