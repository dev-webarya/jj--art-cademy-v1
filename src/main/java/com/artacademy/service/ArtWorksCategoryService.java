package com.artacademy.service;

import com.artacademy.dto.request.ArtWorksCategoryRequestDto;
import com.artacademy.dto.response.ArtWorksCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtWorksCategoryService {

    ArtWorksCategoryResponseDto create(ArtWorksCategoryRequestDto request);

    ArtWorksCategoryResponseDto getById(UUID id);

    Page<ArtWorksCategoryResponseDto> getAll(Pageable pageable);

    List<ArtWorksCategoryResponseDto> getAllRootCategories();

    ArtWorksCategoryResponseDto update(UUID id, ArtWorksCategoryRequestDto request);

    void delete(UUID id);
}
