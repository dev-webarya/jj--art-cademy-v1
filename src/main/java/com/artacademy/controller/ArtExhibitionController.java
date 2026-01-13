package com.artacademy.controller;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import com.artacademy.entity.ArtExhibition;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtExhibitionService;
import com.artacademy.specification.ArtExhibitionSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exhibitions")
@RequiredArgsConstructor
@Slf4j
public class ArtExhibitionController {

    private final ArtExhibitionService artExhibitionService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtExhibitionResponseDto> create(@Valid @RequestBody ArtExhibitionRequestDto request) {
        return new ResponseEntity<>(artExhibitionService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtExhibitionResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(artExhibitionService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtExhibitionResponseDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean ongoing,
            @RequestParam(required = false) LocalDate startsAfter,
            @RequestParam(required = false) UUID categoryId,
            Pageable pageable) {

        Specification<ArtExhibition> spec = Specification.where(ArtExhibitionSpecification.hasName(name))
                .and(ArtExhibitionSpecification.hasLocation(location))
                .and(ArtExhibitionSpecification.isActive(isActive))
                .and(ongoing != null && ongoing ? ArtExhibitionSpecification.isOngoing(LocalDate.now()) : null)
                .and(ArtExhibitionSpecification.startsAfter(startsAfter))
                .and(ArtExhibitionSpecification.inCategory(categoryId));

        return ResponseEntity.ok(artExhibitionService.getAll(spec, pageable));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtExhibitionResponseDto> update(@PathVariable UUID id,
            @Valid @RequestBody ArtExhibitionRequestDto request) {
        return ResponseEntity.ok(artExhibitionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artExhibitionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
