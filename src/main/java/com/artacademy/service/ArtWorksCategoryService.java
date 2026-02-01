package com.artacademy.service;

import com.artacademy.dto.request.ArtWorksCategoryRequestDto;
import com.artacademy.dto.response.ArtWorksCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtWorksCategoryService {

    ArtWorksCategoryResponseDto create(ArtWorksCategoryRequestDto request);

    ArtWorksCategoryResponseDto getById(String id);

    Page<ArtWorksCategoryResponseDto> getAll(Pageable pageable);

    ArtWorksCategoryResponseDto update(String id, ArtWorksCategoryRequestDto request);

    void delete(String id);
}
