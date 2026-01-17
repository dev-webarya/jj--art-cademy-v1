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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtClassesCategoryServiceImpl implements ArtClassesCategoryService {

    private final ArtClassesCategoryRepository categoryRepository;
    private final ArtClassesCategoryMapper categoryMapper;

    @Override
    public ArtClassesCategoryResponseDto create(ArtClassesCategoryRequestDto request) {
        log.info("Creating ArtClassesCategory: {}", request.getName());
        ArtClassesCategory entity = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            ArtClassesCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtClassesCategory", "id", request.getParentId()));
            entity.setParent(ArtClassesCategory.CategoryRef.builder()
                    .categoryId(parent.getId())
                    .name(parent.getName())
                    .build());
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    public ArtClassesCategoryResponseDto getById(String id) {
        log.debug("Fetching ArtClassesCategory by ID: {}", id);
        ArtClassesCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClassesCategory", "id", id));
        return categoryMapper.toDto(entity);
    }

    @Override
    public Page<ArtClassesCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public List<ArtClassesCategoryResponseDto> getAllRootCategories() {
        return categoryRepository.findAll().stream()
                .filter(c -> c.getParent() == null)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtClassesCategoryResponseDto update(String id, ArtClassesCategoryRequestDto request) {
        log.info("Updating ArtClassesCategory ID: {}", id);
        ArtClassesCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClassesCategory", "id", id));

        categoryMapper.updateEntity(request, entity);

        if (request.getParentId() != null) {
            ArtClassesCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtClassesCategory", "id", request.getParentId()));
            entity.setParent(ArtClassesCategory.CategoryRef.builder()
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
        log.warn("Deleting ArtClassesCategory ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtClassesCategory", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}
