package com.artacademy.service;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import com.artacademy.entity.ArtExhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface ArtExhibitionService {
    ArtExhibitionResponseDto create(ArtExhibitionRequestDto request);

    ArtExhibitionResponseDto getById(UUID id);

    Page<ArtExhibitionResponseDto> getAll(Specification<ArtExhibition> spec, Pageable pageable);

    ArtExhibitionResponseDto update(UUID id, ArtExhibitionRequestDto request);

    void delete(UUID id);
}
