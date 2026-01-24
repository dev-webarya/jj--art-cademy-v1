package com.artacademy.service;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtClassesService {
    ArtClassesResponseDto create(ArtClassesRequestDto request);

    ArtClassesResponseDto getById(String id);

    Page<ArtClassesResponseDto> getAll(Pageable pageable);

    ArtClassesResponseDto update(String id, ArtClassesRequestDto request);

    void delete(String id);
}
