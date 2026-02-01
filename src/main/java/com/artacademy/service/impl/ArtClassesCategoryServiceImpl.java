package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtClassesCategoryRequestDto;
import com.artacademy.dto.response.ArtClassesCategoryResponseDto;
import com.artacademy.entity.ArtClassesCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtClassesCategoryMapper;
import com.artacademy.repository.ArtClassesCategoryRepository;
import com.artacademy.service.ArtClassesCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtClassesCategoryServiceImpl implements ArtClassesCategoryService {

    private final ArtClassesCategoryRepository categoryRepository;
    private final ArtClassesCategoryMapper categoryMapper;

    @Override
    public ArtClassesCategoryResponseDto create(ArtClassesCategoryRequestDto request) {
        ArtClassesCategory category = categoryMapper.toEntity(request);
        ArtClassesCategory savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public ArtClassesCategoryResponseDto getById(String id) {
        ArtClassesCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toDto(category);
    }

    @Override
    public Page<ArtClassesCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public ArtClassesCategoryResponseDto update(String id, ArtClassesCategoryRequestDto request) {
        ArtClassesCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryMapper.updateEntity(request, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}
