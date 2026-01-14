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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsCategoryServiceImpl implements ArtMaterialsCategoryService {

    private final ArtMaterialsCategoryRepository categoryRepository;
    private final ArtMaterialsCategoryMapper categoryMapper;

    @Override
    @Transactional
    public ArtMaterialsCategoryResponseDto create(ArtMaterialsCategoryRequestDto request) {
        log.info("Creating ArtMaterialsCategory: {}", request.getName());
        ArtMaterialsCategory entity = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            ArtMaterialsCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtMaterialsCategory", "id", request.getParentId()));
            entity.setParent(parent);
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtMaterialsCategoryResponseDto getById(UUID id) {
        log.debug("Fetching ArtMaterialsCategory by ID: {}", id);
        ArtMaterialsCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterialsCategory", "id", id));
        return categoryMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtMaterialsCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtMaterialsCategoryResponseDto> getAllRootCategories() {
        return categoryRepository.findAll().stream()
                .filter(c -> c.getParent() == null)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArtMaterialsCategoryResponseDto update(UUID id, ArtMaterialsCategoryRequestDto request) {
        log.info("Updating ArtMaterialsCategory ID: {}", id);
        ArtMaterialsCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterialsCategory", "id", id));

        categoryMapper.updateEntity(request, entity);

        if (request.getParentId() != null) {
            ArtMaterialsCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtMaterialsCategory", "id", request.getParentId()));
            entity.setParent(parent);
        } else {
            entity.setParent(null);
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting ArtMaterialsCategory ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtMaterialsCategory", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}
