package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtGalleryCategoryRequestDto;
import com.artacademy.dto.response.ArtGalleryCategoryResponseDto;
import com.artacademy.entity.ArtGalleryCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtGalleryCategoryMapper;
import com.artacademy.repository.ArtGalleryCategoryRepository;
import com.artacademy.service.ArtGalleryCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtGalleryCategoryServiceImpl implements ArtGalleryCategoryService {

    private final ArtGalleryCategoryRepository categoryRepository;
    private final ArtGalleryCategoryMapper categoryMapper;

    @Override
    public ArtGalleryCategoryResponseDto create(ArtGalleryCategoryRequestDto request) {
        ArtGalleryCategory category = categoryMapper.toEntity(request);
        ArtGalleryCategory savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public ArtGalleryCategoryResponseDto getById(String id) {
        ArtGalleryCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toDto(category);
    }

    @Override
    public Page<ArtGalleryCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public ArtGalleryCategoryResponseDto update(String id, ArtGalleryCategoryRequestDto request) {
        ArtGalleryCategory category = categoryRepository.findById(id)
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
