package com.artacademy.service;

import com.artacademy.dto.request.ArtExhibitionCategoryRequestDto;
import com.artacademy.dto.response.ArtExhibitionCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtExhibitionCategoryService {

    ArtExhibitionCategoryResponseDto create(ArtExhibitionCategoryRequestDto request);

    ArtExhibitionCategoryResponseDto getById(UUID id);

    Page<ArtExhibitionCategoryResponseDto> getAll(Pageable pageable);

    List<ArtExhibitionCategoryResponseDto> getAllRootCategories();

    ArtExhibitionCategoryResponseDto update(UUID id, ArtExhibitionCategoryRequestDto request);

    void delete(UUID id);
}
