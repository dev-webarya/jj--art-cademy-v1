package com.artacademy.controller;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import com.artacademy.service.ArtExhibitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/art-exhibitions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Art Exhibitions", description = "Endpoints for managing art exhibitions")
public class ArtExhibitionController {

    private final ArtExhibitionService artExhibitionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Create ArtExhibition")
    public ResponseEntity<ArtExhibitionResponseDto> create(@Valid @RequestBody ArtExhibitionRequestDto request) {
        return new ResponseEntity<>(artExhibitionService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ArtExhibition by ID")
    public ResponseEntity<ArtExhibitionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artExhibitionService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all ArtExhibitions")
    public ResponseEntity<Page<ArtExhibitionResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artExhibitionService.getAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update ArtExhibition")
    public ResponseEntity<ArtExhibitionResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtExhibitionRequestDto request) {
        return ResponseEntity.ok(artExhibitionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ArtExhibition")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        artExhibitionService.delete(id);
    }
}
