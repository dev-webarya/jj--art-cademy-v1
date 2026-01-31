package com.artacademy.service;

import com.artacademy.dto.request.LmsGalleryItemRequestDto;
import com.artacademy.dto.request.LmsGalleryVerifyRequestDto;
import com.artacademy.dto.response.LmsGalleryItemResponseDto;
import com.artacademy.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing gallery items.
 * Includes admin verification workflow.
 */
public interface LmsGalleryService {

    // CRUD operations
    LmsGalleryItemResponseDto create(LmsGalleryItemRequestDto request);

    LmsGalleryItemResponseDto getById(String id);

    LmsGalleryItemResponseDto update(String id, LmsGalleryItemRequestDto request);

    void delete(String id);

    // Query operations
    Page<LmsGalleryItemResponseDto> getAll(Pageable pageable);

    Page<LmsGalleryItemResponseDto> getByUploader(String userId, Pageable pageable);

    Page<LmsGalleryItemResponseDto> getByStatus(VerificationStatus status, Pageable pageable);

    Page<LmsGalleryItemResponseDto> getPublicItems(Pageable pageable);

    Page<LmsGalleryItemResponseDto> getFeaturedItems(Pageable pageable);

    // Verification
    LmsGalleryItemResponseDto verify(String id, LmsGalleryVerifyRequestDto request);

    long countPending();
}
