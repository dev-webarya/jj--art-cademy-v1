package com.artacademy.controller;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.entity.ArtGallery;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtGalleryService;
import com.artacademy.specification.ArtGallerySpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/galleries")
@RequiredArgsConstructor
@Slf4j
public class ArtGalleryController {

    private final ArtGalleryService artGalleryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtGalleryResponseDto> create(@Valid @RequestBody ArtGalleryRequestDto request) {
        return new ResponseEntity<>(artGalleryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtGalleryResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(artGalleryService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtGalleryResponseDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) UUID categoryId,
            Pageable pageable) {

        Specification<ArtGallery> spec = Specification.where(ArtGallerySpecification.hasName(name))
                .and(ArtGallerySpecification.isActive(isActive))
                .and(ArtGallerySpecification.inCategory(categoryId));

        return ResponseEntity.ok(artGalleryService.getAll(spec, pageable));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtGalleryResponseDto> update(@PathVariable UUID id,
            @Valid @RequestBody ArtGalleryRequestDto request) {
        return ResponseEntity.ok(artGalleryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artGalleryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
