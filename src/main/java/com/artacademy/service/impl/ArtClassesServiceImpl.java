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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtClassesServiceImpl implements ArtClassesService {

    private final ArtClassesRepository artClassesRepository;
    private final ArtClassesCategoryRepository categoryRepository;
    private final ArtClassesMapper artClassesMapper;

    @Override
    public ArtClassesResponseDto create(ArtClassesRequestDto request) {
        log.info("Creating art class: {}", request.getName());
        ArtClasses entity = artClassesMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtClassesCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtClassesCategory", "id", request.getCategoryId()));
            // Set embedded category reference
            entity.setCategory(ArtClasses.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artClassesMapper.toDto(artClassesRepository.save(entity));
    }

    @Override
    public ArtClassesResponseDto getById(String id) {
        log.debug("Fetching art class by ID: {}", id);
        ArtClasses entity = artClassesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClasses", "id", id));
        return artClassesMapper.toDto(entity);
    }

    @Override
    public Page<ArtClassesResponseDto> getAll(Pageable pageable) {
        Page<ArtClasses> page = artClassesRepository.findAll(pageable);
        List<ArtClassesResponseDto> dtos = page.getContent().stream()
                .filter(c -> !c.isDeleted())
                .map(artClassesMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public List<ArtClassesResponseDto> getAllActive() {
        return artClassesRepository.findActiveClasses().stream()
                .map(artClassesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtClassesResponseDto> getByCategory(String categoryId) {
        return artClassesRepository.findByCategoryId(categoryId).stream()
                .map(artClassesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtClassesResponseDto> searchByName(String name) {
        return artClassesRepository.searchByName(name).stream()
                .map(artClassesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtClassesResponseDto update(String id, ArtClassesRequestDto request) {
        log.info("Updating art class ID: {}", id);
        ArtClasses entity = artClassesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClasses", "id", id));

        artClassesMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtClassesCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtClassesCategory", "id", request.getCategoryId()));
            entity.setCategory(ArtClasses.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artClassesMapper.toDto(artClassesRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting art class ID: {}", id);
        ArtClasses entity = artClassesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClasses", "id", id));
        entity.setDeleted(true);
        artClassesRepository.save(entity);
    }
}
