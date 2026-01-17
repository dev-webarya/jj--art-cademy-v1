package com.artacademy.service;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtExhibitionService {
    ArtExhibitionResponseDto create(ArtExhibitionRequestDto request);

    ArtExhibitionResponseDto getById(String id);

    Page<ArtExhibitionResponseDto> getAll(Pageable pageable);

    ArtExhibitionResponseDto update(String id, ArtExhibitionRequestDto request);

    void delete(String id);
}
