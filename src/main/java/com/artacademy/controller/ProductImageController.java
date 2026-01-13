package com.artacademy.controller;

import com.artacademy.dto.request.ProductImageRequestDto;
import com.artacademy.dto.response.ProductImageResponseDto;
import com.artacademy.entity.ProductImage;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ProductImageService;
import com.artacademy.specification.ProductImageSpecification;
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
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
@Slf4j
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ProductImageResponseDto> addImageToProduct(
            @Valid @RequestBody ProductImageRequestDto requestDto) {
        log.info("Adding image to product: {}", requestDto.getProductId());
        ProductImageResponseDto createdDto = productImageService.addImageToProduct(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ProductImageResponseDto> getImageById(@PathVariable UUID id) {
        ProductImageResponseDto dto = productImageService.getImageById(id);
        return ResponseEntity.ok(dto);
    }

    // It's common to get images by *product* instead of all images
    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ProductImageResponseDto>> getAllImages(
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) Boolean isPrimary,
            Pageable pageable) {

        Specification<ProductImage> spec = Specification.where(ProductImageSpecification.hasProductId(productId))
                .and(ProductImageSpecification.isPrimary(isPrimary));

        Page<ProductImageResponseDto> dtos = productImageService.getImagesForProduct(productId, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ProductImageResponseDto> updateImageDetails(@PathVariable UUID id,
            @Valid @RequestBody ProductImageRequestDto requestDto) {
        ProductImageResponseDto updatedDto = productImageService.updateImageDetails(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        log.warn("Deleting product image ID: {}", id);
        productImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}