package com.artacademy.controller;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtClassesService;
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
@RequestMapping("/api/v1/art-classes")
@RequiredArgsConstructor
@Slf4j
public class ArtClassesController {

    private final ArtClassesService artClassesService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtClassesResponseDto> create(@Valid @RequestBody ArtClassesRequestDto request) {
        log.info("Creating art class: {}", request.getName());
        return new ResponseEntity<>(artClassesService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtClassesResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artClassesService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtClassesResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artClassesService.getAll(pageable));
    }

    @GetMapping("/active")
    @PublicEndpoint
    public ResponseEntity<List<ArtClassesResponseDto>> getAllActive() {
        return ResponseEntity.ok(artClassesService.getAllActive());
    }

    @GetMapping("/category/{categoryId}")
    @PublicEndpoint
    public ResponseEntity<List<ArtClassesResponseDto>> getByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(artClassesService.getByCategory(categoryId));
    }

    @GetMapping("/search")
    @PublicEndpoint
    public ResponseEntity<List<ArtClassesResponseDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(artClassesService.searchByName(name));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtClassesResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtClassesRequestDto request) {
        return ResponseEntity.ok(artClassesService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artClassesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
