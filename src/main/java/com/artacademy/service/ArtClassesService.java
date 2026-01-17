package com.artacademy.service;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtClassesService {
    ArtClassesResponseDto create(ArtClassesRequestDto request);

    ArtClassesResponseDto getById(String id);

    Page<ArtClassesResponseDto> getAll(Pageable pageable);

    List<ArtClassesResponseDto> getAllActive();

    List<ArtClassesResponseDto> getByCategory(String categoryId);

    List<ArtClassesResponseDto> searchByName(String name);

    ArtClassesResponseDto update(String id, ArtClassesRequestDto request);

    void delete(String id);
}
