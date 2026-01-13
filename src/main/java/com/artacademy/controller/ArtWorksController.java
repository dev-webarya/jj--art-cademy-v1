package com.artacademy.controller;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import com.artacademy.entity.ArtWorks;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtWorksService;
import com.artacademy.specification.ArtWorksSpecification;
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
@RequestMapping("/api/v1/artworks")
@RequiredArgsConstructor
@Slf4j
public class ArtWorksController {

    private final ArtWorksService artWorksService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtWorksResponseDto> create(@Valid @RequestBody ArtWorksRequestDto request) {
        log.info("Creating artwork: {}", request.getName());
        return new ResponseEntity<>(artWorksService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtWorksResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(artWorksService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtWorksResponseDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String artistName,
            @RequestParam(required = false) String artMedium,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) UUID categoryId,
            Pageable pageable) {

        Specification<ArtWorks> spec = Specification.where(ArtWorksSpecification.hasName(name))
                .and(ArtWorksSpecification.hasArtistName(artistName))
                .and(ArtWorksSpecification.hasArtMedium(artMedium))
                .and(ArtWorksSpecification.isActive(isActive))
                .and(ArtWorksSpecification.priceBetween(minPrice, maxPrice))
                .and(ArtWorksSpecification.inCategory(categoryId));

        return ResponseEntity.ok(artWorksService.getAll(spec, pageable));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtWorksResponseDto> update(@PathVariable UUID id,
            @Valid @RequestBody ArtWorksRequestDto request) {
        log.info("Updating artwork ID: {}", id);
        return ResponseEntity.ok(artWorksService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.warn("Deleting artwork ID: {}", id);
        artWorksService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/view")
    @PublicEndpoint
    public ResponseEntity<ArtWorksResponseDto> incrementViews(@PathVariable UUID id) {
        return ResponseEntity.ok(artWorksService.incrementViews(id));
    }

    @PostMapping("/{id}/like")
    @PublicEndpoint
    public ResponseEntity<ArtWorksResponseDto> incrementLikes(@PathVariable UUID id) {
        return ResponseEntity.ok(artWorksService.incrementLikes(id));
    }
}
