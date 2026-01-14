package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtExhibitionCategoryRequestDto;
import com.artacademy.dto.response.ArtExhibitionCategoryResponseDto;
import com.artacademy.entity.ArtExhibitionCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtExhibitionCategoryMapper;
import com.artacademy.repository.ArtExhibitionCategoryRepository;
import com.artacademy.service.ArtExhibitionCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtExhibitionCategoryServiceImpl implements ArtExhibitionCategoryService {

    private final ArtExhibitionCategoryRepository categoryRepository;
    private final ArtExhibitionCategoryMapper categoryMapper;

    @Override
    @Transactional
    public ArtExhibitionCategoryResponseDto create(ArtExhibitionCategoryRequestDto request) {
        log.info("Creating ArtExhibitionCategory: {}", request.getName());
        ArtExhibitionCategory entity = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            ArtExhibitionCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtExhibitionCategory", "id", request.getParentId()));
            entity.setParent(parent);
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtExhibitionCategoryResponseDto getById(UUID id) {
        log.debug("Fetching ArtExhibitionCategory by ID: {}", id);
        ArtExhibitionCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtExhibitionCategory", "id", id));
        return categoryMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtExhibitionCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtExhibitionCategoryResponseDto> getAllRootCategories() {
        return categoryRepository.findAll().stream()
                .filter(c -> c.getParent() == null)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArtExhibitionCategoryResponseDto update(UUID id, ArtExhibitionCategoryRequestDto request) {
        log.info("Updating ArtExhibitionCategory ID: {}", id);
        ArtExhibitionCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtExhibitionCategory", "id", id));

        categoryMapper.updateEntity(request, entity);

        if (request.getParentId() != null) {
            ArtExhibitionCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtExhibitionCategory", "id", request.getParentId()));
            entity.setParent(parent);
        } else {
            entity.setParent(null);
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting ArtExhibitionCategory ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtExhibitionCategory", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}
