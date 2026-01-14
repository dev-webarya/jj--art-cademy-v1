package com.artacademy.service;

import com.artacademy.dto.request.ArtClassesCategoryRequestDto;
import com.artacademy.dto.response.ArtClassesCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtClassesCategoryService {

    ArtClassesCategoryResponseDto create(ArtClassesCategoryRequestDto request);

    ArtClassesCategoryResponseDto getById(UUID id);

    Page<ArtClassesCategoryResponseDto> getAll(Pageable pageable);

    List<ArtClassesCategoryResponseDto> getAllRootCategories();

    ArtClassesCategoryResponseDto update(UUID id, ArtClassesCategoryRequestDto request);

    void delete(UUID id);
}
