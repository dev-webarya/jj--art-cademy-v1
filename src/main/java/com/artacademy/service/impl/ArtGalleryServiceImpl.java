package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.entity.ArtGallery;
import com.artacademy.entity.ArtGalleryCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtGalleryMapper;
import com.artacademy.repository.ArtGalleryCategoryRepository;
import com.artacademy.repository.ArtGalleryRepository;
import com.artacademy.service.ArtGalleryService;
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
public class ArtGalleryServiceImpl implements ArtGalleryService {

    private final ArtGalleryRepository artGalleryRepository;
    private final ArtGalleryCategoryRepository categoryRepository;
    private final ArtGalleryMapper artGalleryMapper;

    @Override
    public ArtGalleryResponseDto create(ArtGalleryRequestDto request) {
        log.info("Creating gallery: {}", request.getName());
        ArtGallery entity = artGalleryMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtGalleryCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtGalleryCategory", "id", request.getCategoryId()));
            entity.setCategory(ArtGallery.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artGalleryMapper.toDto(artGalleryRepository.save(entity));
    }

    @Override
    public ArtGalleryResponseDto getById(String id) {
        log.debug("Fetching gallery by ID: {}", id);
        ArtGallery entity = artGalleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));
        return artGalleryMapper.toDto(entity);
    }

    @Override
    public Page<ArtGalleryResponseDto> getAll(Pageable pageable) {
        Page<ArtGallery> page = artGalleryRepository.findAll(pageable);
        List<ArtGalleryResponseDto> dtos = page.getContent().stream()
                .filter(g -> !g.isDeleted())
                .map(artGalleryMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public List<ArtGalleryResponseDto> getAllActive() {
        return artGalleryRepository.findActiveGalleries().stream()
                .map(artGalleryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtGalleryResponseDto> getByCategory(String categoryId) {
        return artGalleryRepository.findByCategoryId(categoryId).stream()
                .map(artGalleryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtGalleryResponseDto update(String id, ArtGalleryRequestDto request) {
        log.info("Updating gallery ID: {}", id);
        ArtGallery entity = artGalleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));

        artGalleryMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtGalleryCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtGalleryCategory", "id", request.getCategoryId()));
            entity.setCategory(ArtGallery.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artGalleryMapper.toDto(artGalleryRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting gallery ID: {}", id);
        ArtGallery entity = artGalleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));
        entity.setDeleted(true);
        artGalleryRepository.save(entity);
    }
}
