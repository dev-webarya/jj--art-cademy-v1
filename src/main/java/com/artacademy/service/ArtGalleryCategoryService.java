package com.artacademy.service;

import com.artacademy.dto.request.ArtGalleryCategoryRequestDto;
import com.artacademy.dto.response.ArtGalleryCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtGalleryCategoryService {

    ArtGalleryCategoryResponseDto create(ArtGalleryCategoryRequestDto request);

    ArtGalleryCategoryResponseDto getById(UUID id);

    Page<ArtGalleryCategoryResponseDto> getAll(Pageable pageable);

    List<ArtGalleryCategoryResponseDto> getAllRootCategories();

    ArtGalleryCategoryResponseDto update(UUID id, ArtGalleryCategoryRequestDto request);

    void delete(UUID id);
}
