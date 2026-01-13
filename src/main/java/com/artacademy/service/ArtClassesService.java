package com.artacademy.service;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.entity.ArtClasses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface ArtClassesService {
    ArtClassesResponseDto create(ArtClassesRequestDto request);

    ArtClassesResponseDto getById(UUID id);

    Page<ArtClassesResponseDto> getAll(Specification<ArtClasses> spec, Pageable pageable);

    ArtClassesResponseDto update(UUID id, ArtClassesRequestDto request);

    void delete(UUID id);
}
