package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtWorksRequestDto;
import com.artacademy.dto.response.ArtWorksResponseDto;
import com.artacademy.entity.ArtWorks;
import com.artacademy.entity.ArtWorksCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtWorksMapper;
import com.artacademy.repository.ArtWorksCategoryRepository;
import com.artacademy.repository.ArtWorksRepository;
import com.artacademy.service.ArtWorksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtWorksServiceImpl implements ArtWorksService {

    private final ArtWorksRepository artWorksRepository;
    private final ArtWorksCategoryRepository categoryRepository;
    private final ArtWorksMapper artWorksMapper;

    @Override
    public ArtWorksResponseDto create(ArtWorksRequestDto request) {
        log.info("Creating artwork: {}", request.getName());
        ArtWorks entity = artWorksMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtWorksCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtWorksCategory", "id", request.getCategoryId()));
            entity.setCategory(ArtWorks.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        entity.setViews(0);
        entity.setLikes(0);

        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }

    @Override
    public ArtWorksResponseDto getById(String id) {
        log.debug("Fetching artwork by ID: {}", id);
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        return artWorksMapper.toDto(entity);
    }

    @Override
    public Page<ArtWorksResponseDto> getAll(Pageable pageable) {
        Page<ArtWorks> page = artWorksRepository.findAll(pageable);
        List<ArtWorksResponseDto> dtos = page.getContent().stream()
                .filter(w -> !w.isDeleted())
                .map(artWorksMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public List<ArtWorksResponseDto> getAllActive() {
        return artWorksRepository.findActiveWorks().stream()
                .map(artWorksMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtWorksResponseDto> getByCategory(String categoryId) {
        return artWorksRepository.findByCategoryId(categoryId).stream()
                .map(artWorksMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtWorksResponseDto> searchByName(String name) {
        return artWorksRepository.searchByName(name).stream()
                .map(artWorksMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtWorksResponseDto> searchByArtist(String artistName) {
        return artWorksRepository.searchByArtist(artistName).stream()
                .map(artWorksMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtWorksResponseDto update(String id, ArtWorksRequestDto request) {
        log.info("Updating artwork ID: {}", id);
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));

        artWorksMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtWorksCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtWorksCategory", "id", request.getCategoryId()));
            entity.setCategory(ArtWorks.CategoryRef.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .build());
        }

        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting artwork ID: {}", id);
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        entity.setDeleted(true);
        artWorksRepository.save(entity);
    }

    @Override
    public ArtWorksResponseDto incrementViews(String id) {
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        entity.setViews(entity.getViews() + 1);
        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }

    @Override
    public ArtWorksResponseDto incrementLikes(String id) {
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        entity.setLikes(entity.getLikes() + 1);
        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }
}
