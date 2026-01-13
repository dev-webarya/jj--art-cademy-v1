package com.artacademy.service;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import com.artacademy.entity.ArtWorks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface ArtWorksService {
    ArtWorksResponseDto create(ArtWorksRequestDto request);

    ArtWorksResponseDto getById(UUID id);

    Page<ArtWorksResponseDto> getAll(Specification<ArtWorks> spec, Pageable pageable);

    ArtWorksResponseDto update(UUID id, ArtWorksRequestDto request);

    void delete(UUID id);

    ArtWorksResponseDto incrementViews(UUID id);

    ArtWorksResponseDto incrementLikes(UUID id);
}
