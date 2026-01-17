package com.artacademy.controller;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtExhibitionService;
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
@RequestMapping("/api/v1/art-exhibitions")
@RequiredArgsConstructor
@Slf4j
public class ArtExhibitionController {

    private final ArtExhibitionService artExhibitionService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ArtExhibitionResponseDto> create(@Valid @RequestBody ArtExhibitionRequestDto request) {
        log.info("Creating exhibition: {}", request.getName());
        return new ResponseEntity<>(artExhibitionService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ArtExhibitionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artExhibitionService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtExhibitionResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artExhibitionService.getAll(pageable));
    }

    @GetMapping("/active")
    @PublicEndpoint
    public ResponseEntity<List<ArtExhibitionResponseDto>> getAllActive() {
        return ResponseEntity.ok(artExhibitionService.getAllActive());
    }

    @GetMapping("/category/{categoryId}")
    @PublicEndpoint
    public ResponseEntity<List<ArtExhibitionResponseDto>> getByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(artExhibitionService.getByCategory(categoryId));
    }

    @GetMapping("/current")
    @PublicEndpoint
    public ResponseEntity<List<ArtExhibitionResponseDto>> getCurrentExhibitions() {
        return ResponseEntity.ok(artExhibitionService.getCurrentExhibitions());
    }

    @GetMapping("/upcoming")
    @PublicEndpoint
    public ResponseEntity<List<ArtExhibitionResponseDto>> getUpcomingExhibitions() {
        return ResponseEntity.ok(artExhibitionService.getUpcomingExhibitions());
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtExhibitionResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtExhibitionRequestDto request) {
        return ResponseEntity.ok(artExhibitionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artExhibitionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
