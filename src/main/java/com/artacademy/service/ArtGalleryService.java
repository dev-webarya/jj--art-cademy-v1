package com.artacademy.service;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtGalleryService {
    ArtGalleryResponseDto create(ArtGalleryRequestDto request);

    ArtGalleryResponseDto getById(String id);

    Page<ArtGalleryResponseDto> getAll(Pageable pageable);

    ArtGalleryResponseDto update(String id, ArtGalleryRequestDto request);

    void delete(String id);

    ArtGalleryResponseDto verifyGallery(String id, VerificationStatus status);

    Page<ArtGalleryResponseDto> getMyGalleries(Pageable pageable);

    Page<ArtGalleryResponseDto> getMyGalleriesByStatus(VerificationStatus status, Pageable pageable);
}
