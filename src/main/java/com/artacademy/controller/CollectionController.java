package com.artacademy.controller;

import com.artacademy.dto.request.CollectionRequestDto;
import com.artacademy.dto.response.CollectionResponseDto;
import com.artacademy.entity.Collection;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.CollectionService;
import com.artacademy.specification.CollectionSpecification;
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
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
@Slf4j
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<CollectionResponseDto> createCollection(@Valid @RequestBody CollectionRequestDto requestDto) {
        log.info("Creating collection: {}", requestDto.getName());
        CollectionResponseDto createdDto = collectionService.createCollection(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<CollectionResponseDto> getCollectionById(@PathVariable UUID id) {
        CollectionResponseDto dto = collectionService.getCollectionById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<CollectionResponseDto>> getAllCollections(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        Specification<Collection> spec = Specification.where(CollectionSpecification.hasName(name));

        Page<CollectionResponseDto> dtos = collectionService.getAllCollections(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<CollectionResponseDto> updateCollection(@PathVariable UUID id,
            @Valid @RequestBody CollectionRequestDto requestDto) {
        CollectionResponseDto updatedDto = collectionService.updateCollection(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteCollection(@PathVariable UUID id) {
        log.warn("Deleting collection ID: {}", id);
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }
}