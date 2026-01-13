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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtGalleryServiceImpl implements ArtGalleryService {

    private final ArtGalleryRepository artGalleryRepository;
    private final ArtGalleryCategoryRepository categoryRepository;
    private final ArtGalleryMapper artGalleryMapper;

    @Override
    @Transactional
    public ArtGalleryResponseDto create(ArtGalleryRequestDto request) {
        log.info("Creating gallery: {}", request.getName());
        ArtGallery entity = artGalleryMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtGalleryCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtGalleryCategory", "id", request.getCategoryId()));
            entity.setArtGalleryCategory(category);
        }

        return artGalleryMapper.toDto(artGalleryRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtGalleryResponseDto getById(UUID id) {
        ArtGallery entity = artGalleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));
        return artGalleryMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtGalleryResponseDto> getAll(Specification<ArtGallery> spec, Pageable pageable) {
        return artGalleryRepository.findAll(spec, pageable).map(artGalleryMapper::toDto);
    }

    @Override
    @Transactional
    public ArtGalleryResponseDto update(UUID id, ArtGalleryRequestDto request) {
        log.info("Updating gallery ID: {}", id);
        ArtGallery entity = artGalleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));

        artGalleryMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtGalleryCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtGalleryCategory", "id", request.getCategoryId()));
            entity.setArtGalleryCategory(category);
        }

        return artGalleryMapper.toDto(artGalleryRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting gallery ID: {}", id);
        if (!artGalleryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtGallery", "id", id);
        }
        artGalleryRepository.deleteById(id);
    }
}
