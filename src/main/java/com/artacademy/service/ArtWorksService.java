package com.artacademy.service;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtWorksService {
    ArtWorksResponseDto create(ArtWorksRequestDto request);

    ArtWorksResponseDto getById(String id);

    Page<ArtWorksResponseDto> getAll(Pageable pageable);

    ArtWorksResponseDto update(String id, ArtWorksRequestDto request);

    void delete(String id);

    ArtWorksResponseDto incrementViews(String id);

    ArtWorksResponseDto incrementLikes(String id);
}
