package com.artacademy.service.impl;

import com.artacademy.dto.request.CategoryRequestDto;
import com.artacademy.dto.response.CategoryResponseDto;
import com.artacademy.entity.Category;
import com.artacademy.exception.DuplicateResourceException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.CategoryMapper;
import com.artacademy.repository.CategoryRepository;
import com.artacademy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        log.info("Creating category: {}", categoryRequestDto.getName());
        if (categoryNameExists(categoryRequestDto.getName())) {
            throw new DuplicateResourceException(
                    "Category with name '" + categoryRequestDto.getName() + "' already exists.");
        }

        Category category = categoryMapper.toEntity(categoryRequestDto);

        if (categoryRequestDto.getParentId() != null) {
            Category parent = findCategoryById(categoryRequestDto.getParentId());
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDto> getAllCategories(Specification<Category> spec, Pageable pageable) {
        return categoryRepository.findAll(spec, pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(UUID id, CategoryRequestDto categoryRequestDto) {
        Category existingCategory = findCategoryById(id);

        if (!existingCategory.getName().equalsIgnoreCase(categoryRequestDto.getName())
                && categoryNameExists(categoryRequestDto.getName())) {
            throw new DuplicateResourceException(
                    "Category with name '" + categoryRequestDto.getName() + "' already exists.");
        }

        categoryMapper.updateEntityFromDto(categoryRequestDto, existingCategory);

        if (categoryRequestDto.getParentId() != null) {
            if (categoryRequestDto.getParentId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be its own parent.");
            }
            Category parent = findCategoryById(categoryRequestDto.getParentId());
            existingCategory.setParent(parent);
        } else {
            existingCategory.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        log.warn("Deleting category ID: {}", id);
        Category category = findCategoryById(id);

        if (!category.getSubcategories().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Cannot delete category. It has active subcategories. Please reassign or delete them first.");
        }

        if (!category.getProducts().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Cannot delete category. It has products associated with it. Please reassign products first.");
        }

        categoryRepository.delete(category);
    }

    private Category findCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private boolean categoryNameExists(String name) {
        // Use specification to check existence efficiently
        return categoryRepository.exists((root, query, cb) -> cb.equal(cb.lower(root.get("name")), name.toLowerCase()));
    }
}