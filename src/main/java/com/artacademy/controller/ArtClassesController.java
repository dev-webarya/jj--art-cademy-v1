package com.artacademy.controller;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.entity.ArtClasses;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtClassesService;
import com.artacademy.specification.ArtClassesSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

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
    public ResponseEntity<ArtClassesResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(artClassesService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtClassesResponseDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String proficiency,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) UUID categoryId,
            Pageable pageable) {

        Specification<ArtClasses> spec = Specification.where(ArtClassesSpecification.hasName(name))
                .and(ArtClassesSpecification.hasProficiency(proficiency))
                .and(ArtClassesSpecification.isActive(isActive))
                .and(ArtClassesSpecification.priceBetween(minPrice, maxPrice))
                .and(ArtClassesSpecification.inCategory(categoryId));

        return ResponseEntity.ok(artClassesService.getAll(spec, pageable));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtClassesResponseDto> update(@PathVariable UUID id,
            @Valid @RequestBody ArtClassesRequestDto request) {
        return ResponseEntity.ok(artClassesService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artClassesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
