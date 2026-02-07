package com.artacademy.service;

import com.artacademy.dto.request.ArtExhibitionCategoryRequestDto;
import com.artacademy.dto.response.ArtExhibitionCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtExhibitionCategoryService {

    ArtExhibitionCategoryResponseDto create(ArtExhibitionCategoryRequestDto request);

    ArtExhibitionCategoryResponseDto getById(String id);

    Page<ArtExhibitionCategoryResponseDto> getAll(Pageable pageable);

    ArtExhibitionCategoryResponseDto update(String id, ArtExhibitionCategoryRequestDto request);

    void delete(String id);
}
