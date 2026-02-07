package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtWorksCategoryRequestDto;
import com.artacademy.dto.response.ArtWorksCategoryResponseDto;
import com.artacademy.entity.ArtWorksCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtWorksCategoryMapper;
import com.artacademy.repository.ArtWorksCategoryRepository;
import com.artacademy.service.ArtWorksCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtWorksCategoryServiceImpl implements ArtWorksCategoryService {

    private final ArtWorksCategoryRepository categoryRepository;
    private final ArtWorksCategoryMapper categoryMapper;

    @Override
    public ArtWorksCategoryResponseDto create(ArtWorksCategoryRequestDto request) {
        ArtWorksCategory category = categoryMapper.toEntity(request);
        ArtWorksCategory savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public ArtWorksCategoryResponseDto getById(String id) {
        ArtWorksCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toDto(category);
    }

    @Override
    public Page<ArtWorksCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public ArtWorksCategoryResponseDto update(String id, ArtWorksCategoryRequestDto request) {
        ArtWorksCategory category = categoryRepository.findById(id)
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
