package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import com.artacademy.entity.ArtExhibition;
import com.artacademy.entity.ArtExhibitionCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtExhibitionMapper;
import com.artacademy.repository.ArtExhibitionCategoryRepository;
import com.artacademy.repository.ArtExhibitionRepository;
import com.artacademy.service.ArtExhibitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtExhibitionServiceImpl implements ArtExhibitionService {

    private final ArtExhibitionRepository artExhibitionRepository;
    private final ArtExhibitionCategoryRepository categoryRepository;
    private final ArtExhibitionMapper artExhibitionMapper;

    @Override
    public ArtExhibitionResponseDto create(ArtExhibitionRequestDto request) {
        log.info("Creating exhibition: {}", request.getName());
        ArtExhibition entity = artExhibitionMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtExhibitionCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtExhibitionCategory", "id",
                                    request.getCategoryId()));
            entity.setCategory(ArtExhibition.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artExhibitionMapper.toDto(artExhibitionRepository.save(entity));
    }

    @Override
    public ArtExhibitionResponseDto getById(String id) {
        log.debug("Fetching exhibition by ID: {}", id);
        ArtExhibition entity = artExhibitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtExhibition", "id", id));
        return artExhibitionMapper.toDto(entity);
    }

    @Override
    public Page<ArtExhibitionResponseDto> getAll(Pageable pageable) {
        Page<ArtExhibition> page = artExhibitionRepository.findAll(pageable);
        List<ArtExhibitionResponseDto> dtos = page.getContent().stream()
                .filter(e -> !e.isDeleted())
                .map(artExhibitionMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public List<ArtExhibitionResponseDto> getAllActive() {
        return artExhibitionRepository.findActiveExhibitions().stream()
                .map(artExhibitionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtExhibitionResponseDto> getByCategory(String categoryId) {
        return artExhibitionRepository.findByCategoryId(categoryId).stream()
                .map(artExhibitionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtExhibitionResponseDto> getCurrentExhibitions() {
        return artExhibitionRepository.findCurrentExhibitions(LocalDate.now()).stream()
                .map(artExhibitionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtExhibitionResponseDto> getUpcomingExhibitions() {
        return artExhibitionRepository.findUpcomingExhibitions(LocalDate.now()).stream()
                .map(artExhibitionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtExhibitionResponseDto update(String id, ArtExhibitionRequestDto request) {
        log.info("Updating exhibition ID: {}", id);
        ArtExhibition entity = artExhibitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtExhibition", "id", id));

        artExhibitionMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtExhibitionCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtExhibitionCategory", "id",
                                    request.getCategoryId()));
            entity.setCategory(ArtExhibition.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artExhibitionMapper.toDto(artExhibitionRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting exhibition ID: {}", id);
        ArtExhibition entity = artExhibitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtExhibition", "id", id));
        entity.setDeleted(true);
        artExhibitionRepository.save(entity);
    }
}
