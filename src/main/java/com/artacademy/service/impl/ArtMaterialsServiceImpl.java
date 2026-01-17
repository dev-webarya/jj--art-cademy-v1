package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.entity.ArtMaterials;
import com.artacademy.entity.ArtMaterialsCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtMaterialsMapper;
import com.artacademy.repository.ArtMaterialsCategoryRepository;
import com.artacademy.repository.ArtMaterialsRepository;
import com.artacademy.service.ArtMaterialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsServiceImpl implements ArtMaterialsService {

    private final ArtMaterialsRepository artMaterialsRepository;
    private final ArtMaterialsCategoryRepository categoryRepository;
    private final ArtMaterialsMapper artMaterialsMapper;

    @Override
    public ArtMaterialsResponseDto create(ArtMaterialsRequestDto request) {
        log.info("Creating material: {}", request.getName());
        ArtMaterials entity = artMaterialsMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtMaterialsCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtMaterialsCategory", "id", request.getCategoryId()));
            entity.setCategory(ArtMaterials.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artMaterialsMapper.toDto(artMaterialsRepository.save(entity));
    }

    @Override
    public ArtMaterialsResponseDto getById(String id) {
        log.debug("Fetching material by ID: {}", id);
        ArtMaterials entity = artMaterialsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));
        return artMaterialsMapper.toDto(entity);
    }

    @Override
    public Page<ArtMaterialsResponseDto> getAll(Pageable pageable) {
        Page<ArtMaterials> page = artMaterialsRepository.findAll(pageable);
        List<ArtMaterialsResponseDto> dtos = page.getContent().stream()
                .filter(m -> !m.isDeleted())
                .map(artMaterialsMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public List<ArtMaterialsResponseDto> getAllActive() {
        return artMaterialsRepository.findActiveMaterials().stream()
                .map(artMaterialsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtMaterialsResponseDto> getByCategory(String categoryId) {
        return artMaterialsRepository.findByCategoryId(categoryId).stream()
                .map(artMaterialsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtMaterialsResponseDto> searchByName(String name) {
        return artMaterialsRepository.searchByName(name).stream()
                .map(artMaterialsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtMaterialsResponseDto> getInStock() {
        return artMaterialsRepository.findInStock().stream()
                .map(artMaterialsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtMaterialsResponseDto update(String id, ArtMaterialsRequestDto request) {
        log.info("Updating material ID: {}", id);
        ArtMaterials entity = artMaterialsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));

        artMaterialsMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtMaterialsCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtMaterialsCategory", "id", request.getCategoryId()));
            entity.setCategory(ArtMaterials.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artMaterialsMapper.toDto(artMaterialsRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting material ID: {}", id);
        ArtMaterials entity = artMaterialsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));
        entity.setDeleted(true);
        artMaterialsRepository.save(entity);
    }
}
