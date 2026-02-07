package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtMaterialsCategoryRequestDto;
import com.artacademy.dto.response.ArtMaterialsCategoryResponseDto;
import com.artacademy.entity.ArtMaterialsCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtMaterialsCategoryMapper;
import com.artacademy.repository.ArtMaterialsCategoryRepository;
import com.artacademy.service.ArtMaterialsCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsCategoryServiceImpl implements ArtMaterialsCategoryService {

    private final ArtMaterialsCategoryRepository categoryRepository;
    private final ArtMaterialsCategoryMapper categoryMapper;

    @Override
    public ArtMaterialsCategoryResponseDto create(ArtMaterialsCategoryRequestDto request) {
        ArtMaterialsCategory category = categoryMapper.toEntity(request);
        ArtMaterialsCategory savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public ArtMaterialsCategoryResponseDto getById(String id) {
        ArtMaterialsCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toDto(category);
    }

    @Override
    public Page<ArtMaterialsCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public ArtMaterialsCategoryResponseDto update(String id, ArtMaterialsCategoryRequestDto request) {
        ArtMaterialsCategory category = categoryRepository.findById(id)
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
