package com.artacademy.service;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.entity.ArtMaterials;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface ArtMaterialsService {
    ArtMaterialsResponseDto create(ArtMaterialsRequestDto request);

    ArtMaterialsResponseDto getById(UUID id);

    Page<ArtMaterialsResponseDto> getAll(Specification<ArtMaterials> spec, Pageable pageable);

    ArtMaterialsResponseDto update(UUID id, ArtMaterialsRequestDto request);

    void delete(UUID id);
}
