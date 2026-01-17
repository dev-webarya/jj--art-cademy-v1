package com.artacademy.service;

import com.artacademy.dto.request.ArtMaterialsCategoryRequestDto;
import com.artacademy.dto.response.ArtMaterialsCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtMaterialsCategoryService {

    ArtMaterialsCategoryResponseDto create(ArtMaterialsCategoryRequestDto request);

    ArtMaterialsCategoryResponseDto getById(String id);

    Page<ArtMaterialsCategoryResponseDto> getAll(Pageable pageable);

    List<ArtMaterialsCategoryResponseDto> getAllRootCategories();

    ArtMaterialsCategoryResponseDto update(String id, ArtMaterialsCategoryRequestDto request);

    void delete(String id);
}
