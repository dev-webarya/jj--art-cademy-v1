package com.artacademy.service;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.entity.ArtGallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface ArtGalleryService {
    ArtGalleryResponseDto create(ArtGalleryRequestDto request);

    ArtGalleryResponseDto getById(UUID id);

    Page<ArtGalleryResponseDto> getAll(Specification<ArtGallery> spec, Pageable pageable);

    ArtGalleryResponseDto update(UUID id, ArtGalleryRequestDto request);

    void delete(UUID id);
}
