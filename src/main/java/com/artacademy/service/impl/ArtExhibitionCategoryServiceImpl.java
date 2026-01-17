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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtExhibitionCategoryServiceImpl implements ArtExhibitionCategoryService {

    private final ArtExhibitionCategoryRepository categoryRepository;
    private final ArtExhibitionCategoryMapper categoryMapper;

    @Override
    public ArtExhibitionCategoryResponseDto create(ArtExhibitionCategoryRequestDto request) {
        ArtExhibitionCategory category = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            ArtExhibitionCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "parentId", request.getParentId()));
            category.setParentId(parent.getId());
            category.setParentName(parent.getName());
        }

        ArtExhibitionCategory savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public ArtExhibitionCategoryResponseDto getById(String id) {
        ArtExhibitionCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toDto(category);
    }

    @Override
    public Page<ArtExhibitionCategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public List<ArtExhibitionCategoryResponseDto> getAllRootCategories() {
        return categoryMapper.toDtoList(categoryRepository.findByParentIdIsNull());
    }

    @Override
    public ArtExhibitionCategoryResponseDto update(String id, ArtExhibitionCategoryRequestDto request) {
        ArtExhibitionCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryMapper.updateEntity(request, category);

        if (request.getParentId() != null) {
            ArtExhibitionCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "parentId", request.getParentId()));
            category.setParentId(parent.getId());
            category.setParentName(parent.getName());
        } else {
            category.setParentId(null);
            category.setParentName(null);
        }

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
