package com.artacademy.service;

import com.artacademy.dto.request.ArtGalleryCategoryRequestDto;
import com.artacademy.dto.response.ArtGalleryCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtGalleryCategoryService {

    ArtGalleryCategoryResponseDto create(ArtGalleryCategoryRequestDto request);

    ArtGalleryCategoryResponseDto getById(String id);

    Page<ArtGalleryCategoryResponseDto> getAll(Pageable pageable);

    List<ArtGalleryCategoryResponseDto> getAllRootCategories();

    ArtGalleryCategoryResponseDto update(String id, ArtGalleryCategoryRequestDto request);

    void delete(String id);
}
