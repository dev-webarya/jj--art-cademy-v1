package com.artacademy.service;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtMaterialsService {
    ArtMaterialsResponseDto create(ArtMaterialsRequestDto request);

    ArtMaterialsResponseDto getById(String id);

    Page<ArtMaterialsResponseDto> getAll(Pageable pageable);

    List<ArtMaterialsResponseDto> getAllActive();

    List<ArtMaterialsResponseDto> getByCategory(String categoryId);

    List<ArtMaterialsResponseDto> searchByName(String name);

    List<ArtMaterialsResponseDto> getInStock();

    ArtMaterialsResponseDto update(String id, ArtMaterialsRequestDto request);

    void delete(String id);
}
