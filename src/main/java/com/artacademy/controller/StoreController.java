package com.artacademy.controller;

import com.artacademy.dto.request.StoreRequestDto;
import com.artacademy.dto.response.StoreResponseDto;
import com.artacademy.entity.Store;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.StoreService;
import com.artacademy.specification.StoreSpecification;
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
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    @AdminOnly
    public ResponseEntity<StoreResponseDto> createStore(@Valid @RequestBody StoreRequestDto requestDto) {
        log.info("Creating store: {}", requestDto.getName());
        StoreResponseDto createdDto = storeService.createStore(requestDto);
        log.debug("Store created with ID: {}", createdDto.getId());
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable UUID id) {
        StoreResponseDto dto = storeService.getStoreById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<StoreResponseDto>> getAllStores(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            Pageable pageable) {

        Specification<Store> spec = Specification.where(StoreSpecification.hasName(name))
                .and(StoreSpecification.hasAddress(address));

        Page<StoreResponseDto> dtos = storeService.getAllStores(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @AdminOnly
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable UUID id,
            @Valid @RequestBody StoreRequestDto requestDto) {
        StoreResponseDto updatedDto = storeService.updateStore(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteStore(@PathVariable UUID id) {
        log.warn("Deleting store ID: {}", id);
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}