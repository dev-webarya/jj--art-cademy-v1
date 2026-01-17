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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtGalleryCategoryServiceImpl implements ArtGalleryCategoryService {

    private final ArtGalleryCategoryRepository categoryRepository;
    private final ArtGalleryCategoryMapper categoryMapper;

    @Override
    public ArtGalleryCategoryResponseDto create(ArtGalleryCategoryRequestDto request) {
        log.info("Creating ArtGalleryCategory: {}", request.getName());
        ArtGalleryCategory entity = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            ArtGalleryCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtGalleryCategory", "id", request.getParentId()));
            entity.setParent(ArtGalleryCategory.CategoryRef.builder()
                    .categoryId(parent.getId())
                    .name(parent.getName())
                    .build());
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    public ArtGalleryCategoryResponseDto getById(String id) {
        log.debug("Fetching ArtGalleryCategory by ID: {}", id);
        ArtGalleryCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGalleryCategory", "id", id));
        return categoryMapper.toDto(entity);
    }

    @Override
    public Page<ArtGalleryCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public List<ArtGalleryCategoryResponseDto> getAllRootCategories() {
        return categoryRepository.findAll().stream()
                .filter(c -> c.getParent() == null)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtGalleryCategoryResponseDto update(String id, ArtGalleryCategoryRequestDto request) {
        log.info("Updating ArtGalleryCategory ID: {}", id);
        ArtGalleryCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGalleryCategory", "id", id));

        categoryMapper.updateEntity(request, entity);

        if (request.getParentId() != null) {
            ArtGalleryCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtGalleryCategory", "id", request.getParentId()));
            entity.setParent(ArtGalleryCategory.CategoryRef.builder()
                    .categoryId(parent.getId())
                    .name(parent.getName())
                    .build());
        } else {
            entity.setParent(null);
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting ArtGalleryCategory ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtGalleryCategory", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}
