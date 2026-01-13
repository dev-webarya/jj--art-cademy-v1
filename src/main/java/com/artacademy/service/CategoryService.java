package com.artacademy.service;

import com.artacademy.dto.request.CategoryRequestDto;
import com.artacademy.dto.response.CategoryResponseDto;
import com.artacademy.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    Page<CategoryResponseDto> getAllCategories(Specification<Category> spec, Pageable pageable);

    CategoryResponseDto getCategoryById(UUID id);

    CategoryResponseDto updateCategory(UUID id, CategoryRequestDto categoryRequestDto);

    void deleteCategory(UUID id);
}