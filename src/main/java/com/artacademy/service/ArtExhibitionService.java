package com.artacademy.service;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ArtExhibitionService {
    ArtExhibitionResponseDto create(ArtExhibitionRequestDto request);

    ArtExhibitionResponseDto getById(String id);

    Page<ArtExhibitionResponseDto> getAll(Pageable pageable);

    List<ArtExhibitionResponseDto> getAllActive();

    List<ArtExhibitionResponseDto> getByCategory(String categoryId);

    List<ArtExhibitionResponseDto> getCurrentExhibitions();

    List<ArtExhibitionResponseDto> getUpcomingExhibitions();

    ArtExhibitionResponseDto update(String id, ArtExhibitionRequestDto request);

    void delete(String id);
}
