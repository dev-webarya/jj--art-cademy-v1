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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtWorksCategoryServiceImpl implements ArtWorksCategoryService {

    private final ArtWorksCategoryRepository categoryRepository;
    private final ArtWorksCategoryMapper categoryMapper;

    @Override
    @Transactional
    public ArtWorksCategoryResponseDto create(ArtWorksCategoryRequestDto request) {
        log.info("Creating ArtWorksCategory: {}", request.getName());
        ArtWorksCategory entity = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            ArtWorksCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtWorksCategory", "id", request.getParentId()));
            entity.setParent(parent);
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtWorksCategoryResponseDto getById(UUID id) {
        log.debug("Fetching ArtWorksCategory by ID: {}", id);
        ArtWorksCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorksCategory", "id", id));
        return categoryMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtWorksCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtWorksCategoryResponseDto> getAllRootCategories() {
        return categoryRepository.findAll().stream()
                .filter(c -> c.getParent() == null)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArtWorksCategoryResponseDto update(UUID id, ArtWorksCategoryRequestDto request) {
        log.info("Updating ArtWorksCategory ID: {}", id);
        ArtWorksCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorksCategory", "id", id));

        categoryMapper.updateEntity(request, entity);

        if (request.getParentId() != null) {
            ArtWorksCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtWorksCategory", "id", request.getParentId()));
            entity.setParent(parent);
        } else {
            entity.setParent(null);
        }

        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting ArtWorksCategory ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtWorksCategory", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}
