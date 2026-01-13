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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtWorksServiceImpl implements ArtWorksService {

    private final ArtWorksRepository artWorksRepository;
    private final ArtWorksCategoryRepository categoryRepository;
    private final ArtWorksMapper artWorksMapper;

    @Override
    @Transactional
    public ArtWorksResponseDto create(ArtWorksRequestDto request) {
        log.info("Creating artwork: {}", request.getName());
        ArtWorks entity = artWorksMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtWorksCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtWorksCategory", "id", request.getCategoryId()));
            entity.setCategory(category);
        }

        entity.setViews(0);
        entity.setLikes(0);

        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtWorksResponseDto getById(UUID id) {
        log.debug("Fetching artwork by ID: {}", id);
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        return artWorksMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtWorksResponseDto> getAll(Specification<ArtWorks> spec, Pageable pageable) {
        return artWorksRepository.findAll(spec, pageable).map(artWorksMapper::toDto);
    }

    @Override
    @Transactional
    public ArtWorksResponseDto update(UUID id, ArtWorksRequestDto request) {
        log.info("Updating artwork ID: {}", id);
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));

        artWorksMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtWorksCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("ArtWorksCategory", "id", request.getCategoryId()));
            entity.setCategory(category);
        }

        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting artwork ID: {}", id);
        if (!artWorksRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtWorks", "id", id);
        }
        artWorksRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ArtWorksResponseDto incrementViews(UUID id) {
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        entity.setViews(entity.getViews() + 1);
        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }

    @Override
    @Transactional
    public ArtWorksResponseDto incrementLikes(UUID id) {
        ArtWorks entity = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        entity.setLikes(entity.getLikes() + 1);
        return artWorksMapper.toDto(artWorksRepository.save(entity));
    }
}
