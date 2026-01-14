package com.artacademy.service;

import com.artacademy.dto.request.ArtMaterialsCategoryRequestDto;
import com.artacademy.dto.response.ArtMaterialsCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtMaterialsCategoryService {

    ArtMaterialsCategoryResponseDto create(ArtMaterialsCategoryRequestDto request);

    ArtMaterialsCategoryResponseDto getById(UUID id);

    Page<ArtMaterialsCategoryResponseDto> getAll(Pageable pageable);

    List<ArtMaterialsCategoryResponseDto> getAllRootCategories();

    ArtMaterialsCategoryResponseDto update(UUID id, ArtMaterialsCategoryRequestDto request);

    void delete(UUID id);
}
