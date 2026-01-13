package com.artacademy.service;

import com.artacademy.dto.request.CollectionRequestDto;
import com.artacademy.dto.response.CollectionResponseDto;
import com.artacademy.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CollectionService {

    CollectionResponseDto createCollection(CollectionRequestDto requestDto);

    Page<CollectionResponseDto> getAllCollections(Specification<Collection> spec, Pageable pageable);

    CollectionResponseDto getCollectionById(UUID id);

    CollectionResponseDto updateCollection(UUID id, CollectionRequestDto requestDto);

    void deleteCollection(UUID id);
}