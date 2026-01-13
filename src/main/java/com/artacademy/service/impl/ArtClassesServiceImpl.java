package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.entity.ArtClasses;
import com.artacademy.entity.ArtClassesCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtClassesMapper;
import com.artacademy.repository.ArtClassesCategoryRepository;
import com.artacademy.repository.ArtClassesRepository;
import com.artacademy.service.ArtClassesService;
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
public class ArtClassesServiceImpl implements ArtClassesService {

    private final ArtClassesRepository artClassesRepository;
    private final ArtClassesCategoryRepository categoryRepository;
    private final ArtClassesMapper artClassesMapper;

    @Override
    @Transactional
    public ArtClassesResponseDto create(ArtClassesRequestDto request) {
        log.info("Creating art class: {}", request.getName());
        ArtClasses entity = artClassesMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtClassesCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtClassesCategory", "id", request.getCategoryId()));
            entity.setCategory(category);
        }

        return artClassesMapper.toDto(artClassesRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtClassesResponseDto getById(UUID id) {
        log.debug("Fetching art class by ID: {}", id);
        ArtClasses entity = artClassesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClasses", "id", id));
        return artClassesMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtClassesResponseDto> getAll(Specification<ArtClasses> spec, Pageable pageable) {
        return artClassesRepository.findAll(spec, pageable).map(artClassesMapper::toDto);
    }

    @Override
    @Transactional
    public ArtClassesResponseDto update(UUID id, ArtClassesRequestDto request) {
        log.info("Updating art class ID: {}", id);
        ArtClasses entity = artClassesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClasses", "id", id));

        artClassesMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtClassesCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtClassesCategory", "id", request.getCategoryId()));
            entity.setCategory(category);
        }

        return artClassesMapper.toDto(artClassesRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting art class ID: {}", id);
        if (!artClassesRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtClasses", "id", id);
        }
        artClassesRepository.deleteById(id);
    }
}
