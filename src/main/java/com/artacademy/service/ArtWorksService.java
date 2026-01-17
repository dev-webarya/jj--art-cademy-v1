package com.artacademy.service;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtWorksService {
    ArtWorksResponseDto create(ArtWorksRequestDto request);

    ArtWorksResponseDto getById(String id);

    Page<ArtWorksResponseDto> getAll(Pageable pageable);

    List<ArtWorksResponseDto> getAllActive();

    List<ArtWorksResponseDto> getByCategory(String categoryId);

    List<ArtWorksResponseDto> searchByName(String name);

    List<ArtWorksResponseDto> searchByArtist(String artistName);

    ArtWorksResponseDto update(String id, ArtWorksRequestDto request);

    void delete(String id);

    ArtWorksResponseDto incrementViews(String id);

    ArtWorksResponseDto incrementLikes(String id);
}
