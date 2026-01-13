package com.artacademy.controller;

import com.artacademy.dto.request.ProductRequestDto;
import com.artacademy.dto.response.ProductResponseDto;
import com.artacademy.entity.Product;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.ProductService;
import com.artacademy.specification.ProductSpecification;
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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        log.info("Creating product with SKU: {}", requestDto.getSku());
        ProductResponseDto createdDto = productService.createProduct(requestDto);
        log.debug("Product created with ID: {}", createdDto.getId());
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        log.debug("Fetching product by ID: {}", id);
        ProductResponseDto dto = productService.getProductById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            // Search Params
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            // Relationship Params
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer collectionId,
            @RequestParam(required = false) Integer attributeValueId,
            Pageable pageable) {

        // Start with fetch-all specification to avoid N+1
        Specification<Product> spec = Specification.where(ProductSpecification.fetchAllRelations())
                .and(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasSku(sku))
                .and(ProductSpecification.isActive(isActive))
                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
                .and(ProductSpecification.inCategory(categoryId))
                .and(ProductSpecification.inCollection(collectionId))
                .and(ProductSpecification.hasAttribute(attributeValueId));

        Page<ProductResponseDto> dtos = productService.getAllProducts(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID id,
            @Valid @RequestBody ProductRequestDto requestDto) {
        log.info("Updating product ID: {}", id);
        ProductResponseDto updatedDto = productService.updateProduct(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        log.warn("Deleting product ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}