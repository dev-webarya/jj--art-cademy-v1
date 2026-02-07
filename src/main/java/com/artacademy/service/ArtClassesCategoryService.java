package com.artacademy.service;

import com.artacademy.dto.request.ArtClassesCategoryRequestDto;
import com.artacademy.dto.response.ArtClassesCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtClassesCategoryService {

    ArtClassesCategoryResponseDto create(ArtClassesCategoryRequestDto request);

    ArtClassesCategoryResponseDto getById(String id);

    Page<ArtClassesCategoryResponseDto> getAll(Pageable pageable);

    ArtClassesCategoryResponseDto update(String id, ArtClassesCategoryRequestDto request);

    void delete(String id);
}
