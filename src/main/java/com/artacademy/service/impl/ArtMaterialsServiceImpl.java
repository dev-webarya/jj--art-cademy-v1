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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsServiceImpl implements ArtMaterialsService {

    private final ArtMaterialsRepository artMaterialsRepository;
    private final ArtMaterialsCategoryRepository categoryRepository;
    private final ArtMaterialsMapper artMaterialsMapper;

    @Override
    @Transactional
    public ArtMaterialsResponseDto create(ArtMaterialsRequestDto request) {
        log.info("Creating art material: {}", request.getName());
        ArtMaterials entity = artMaterialsMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtMaterialsCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtMaterialsCategory", "id", request.getCategoryId()));
            entity.setCategory(category);
        }

        return artMaterialsMapper.toDto(artMaterialsRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtMaterialsResponseDto getById(UUID id) {
        log.debug("Fetching art material by ID: {}", id);
        ArtMaterials entity = artMaterialsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));
        return artMaterialsMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtMaterialsResponseDto> getAll(Specification<ArtMaterials> spec, Pageable pageable) {
        return artMaterialsRepository.findAll(spec, pageable).map(artMaterialsMapper::toDto);
    }

    @Override
    @Transactional
    public ArtMaterialsResponseDto update(UUID id, ArtMaterialsRequestDto request) {
        log.info("Updating art material ID: {}", id);
        ArtMaterials entity = artMaterialsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));

        artMaterialsMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtMaterialsCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtMaterialsCategory", "id", request.getCategoryId()));
            entity.setCategory(category);
        }

        return artMaterialsMapper.toDto(artMaterialsRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting art material ID: {}", id);
        if (!artMaterialsRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtMaterials", "id", id);
        }
        artMaterialsRepository.deleteById(id);
    }
}
