package com.artacademy.controller;

import com.artacademy.dto.request.CategoryRequestDto;
import com.artacademy.dto.response.CategoryResponseDto;
import com.artacademy.entity.Category;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.CategoryService;
import com.artacademy.specification.CategorySpecification;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.info("Creating category: {}", categoryRequestDto.getName());
        CategoryResponseDto createdCategory = categoryService.createCategory(categoryRequestDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable UUID id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<CategoryResponseDto>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID parentId,
            @RequestParam(required = false) Boolean isRoot,
            Pageable pageable) {

        // Build dynamic specification based on query params
        Specification<Category> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and(CategorySpecification.hasName(name));
        }
        if (parentId != null) {
            spec = spec.and(CategorySpecification.hasParentId(parentId));
        }
        if (isRoot != null && isRoot) {
            spec = spec.and(CategorySpecification.isRootCategory());
        }

        Page<CategoryResponseDto> categories = categoryService.getAllCategories(spec, pageable);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable UUID id,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(id, categoryRequestDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        log.warn("Deleting category ID: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}