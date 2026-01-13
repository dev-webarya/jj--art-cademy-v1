package com.artacademy.controller;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.entity.ArtMaterials;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtMaterialsService;
import com.artacademy.specification.ArtMaterialsSpecification;
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
@RequestMapping("/api/v1/art-materials")
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsController {

    private final ArtMaterialsService artMaterialsService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtMaterialsResponseDto> create(@Valid @RequestBody ArtMaterialsRequestDto request) {
        log.info("Creating art material: {}", request.getName());
        return new ResponseEntity<>(artMaterialsService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtMaterialsResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(artMaterialsService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtMaterialsResponseDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) UUID categoryId,
            Pageable pageable) {

        Specification<ArtMaterials> spec = Specification.where(ArtMaterialsSpecification.hasName(name))
                .and(ArtMaterialsSpecification.isActive(isActive))
                .and(ArtMaterialsSpecification.hasStock(inStock))
                .and(ArtMaterialsSpecification.priceBetween(minPrice, maxPrice))
                .and(ArtMaterialsSpecification.inCategory(categoryId));

        return ResponseEntity.ok(artMaterialsService.getAll(spec, pageable));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtMaterialsResponseDto> update(@PathVariable UUID id,
            @Valid @RequestBody ArtMaterialsRequestDto request) {
        log.info("Updating art material ID: {}", id);
        return ResponseEntity.ok(artMaterialsService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.warn("Deleting art material ID: {}", id);
        artMaterialsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
