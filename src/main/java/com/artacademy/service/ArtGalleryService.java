package com.artacademy.service;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtGalleryService {
    ArtGalleryResponseDto create(ArtGalleryRequestDto request);

    ArtGalleryResponseDto getById(String id);

    Page<ArtGalleryResponseDto> getAll(Pageable pageable);

    ArtGalleryResponseDto update(String id, ArtGalleryRequestDto request);

    void delete(String id);
}
