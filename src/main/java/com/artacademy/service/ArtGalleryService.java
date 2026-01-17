package com.artacademy.service;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtGalleryService {
    ArtGalleryResponseDto create(ArtGalleryRequestDto request);

    ArtGalleryResponseDto getById(String id);

    Page<ArtGalleryResponseDto> getAll(Pageable pageable);

    List<ArtGalleryResponseDto> getAllActive();

    List<ArtGalleryResponseDto> getByCategory(String categoryId);

    ArtGalleryResponseDto update(String id, ArtGalleryRequestDto request);

    void delete(String id);
}
