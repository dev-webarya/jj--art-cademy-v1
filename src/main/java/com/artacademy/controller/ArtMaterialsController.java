package com.artacademy.controller;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtMaterialsService;
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
@RequestMapping("/api/v1/art-materials")
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsController {

    private final ArtMaterialsService artMaterialsService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtMaterialsResponseDto> create(@Valid @RequestBody ArtMaterialsRequestDto request) {
        log.info("Creating material: {}", request.getName());
        return new ResponseEntity<>(artMaterialsService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtMaterialsResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artMaterialsService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtMaterialsResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artMaterialsService.getAll(pageable));
    }

    @GetMapping("/active")
    @PublicEndpoint
    public ResponseEntity<List<ArtMaterialsResponseDto>> getAllActive() {
        return ResponseEntity.ok(artMaterialsService.getAllActive());
    }

    @GetMapping("/category/{categoryId}")
    @PublicEndpoint
    public ResponseEntity<List<ArtMaterialsResponseDto>> getByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(artMaterialsService.getByCategory(categoryId));
    }

    @GetMapping("/search")
    @PublicEndpoint
    public ResponseEntity<List<ArtMaterialsResponseDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(artMaterialsService.searchByName(name));
    }

    @GetMapping("/in-stock")
    @PublicEndpoint
    public ResponseEntity<List<ArtMaterialsResponseDto>> getInStock() {
        return ResponseEntity.ok(artMaterialsService.getInStock());
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtMaterialsResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtMaterialsRequestDto request) {
        return ResponseEntity.ok(artMaterialsService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artMaterialsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
