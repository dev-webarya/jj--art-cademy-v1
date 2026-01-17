package com.artacademy.controller;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ArtWorksService;
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
@RequestMapping("/api/v1/art-works")
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
    public ResponseEntity<ArtWorksResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(artWorksService.getById(id));
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ArtWorksResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(artWorksService.getAll(pageable));
    }

    @GetMapping("/active")
    @PublicEndpoint
    public ResponseEntity<List<ArtWorksResponseDto>> getAllActive() {
        return ResponseEntity.ok(artWorksService.getAllActive());
    }

    @GetMapping("/category/{categoryId}")
    @PublicEndpoint
    public ResponseEntity<List<ArtWorksResponseDto>> getByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(artWorksService.getByCategory(categoryId));
    }

    @GetMapping("/search")
    @PublicEndpoint
    public ResponseEntity<List<ArtWorksResponseDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(artWorksService.searchByName(name));
    }

    @GetMapping("/artist")
    @PublicEndpoint
    public ResponseEntity<List<ArtWorksResponseDto>> searchByArtist(@RequestParam String artistName) {
        return ResponseEntity.ok(artWorksService.searchByArtist(artistName));
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ArtWorksResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ArtWorksRequestDto request) {
        return ResponseEntity.ok(artWorksService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artWorksService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/view")
    @PublicEndpoint
    public ResponseEntity<ArtWorksResponseDto> incrementViews(@PathVariable String id) {
        return ResponseEntity.ok(artWorksService.incrementViews(id));
    }

    @PatchMapping("/{id}/like")
    @PublicEndpoint
    public ResponseEntity<ArtWorksResponseDto> incrementLikes(@PathVariable String id) {
        return ResponseEntity.ok(artWorksService.incrementLikes(id));
    }
}
