package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtExhibitionRequestDto;
import com.artacademy.dto.response.ArtExhibitionResponseDto;
import com.artacademy.entity.ArtExhibition;
import com.artacademy.entity.ArtExhibitionCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtExhibitionMapper;
import com.artacademy.repository.ArtExhibitionCategoryRepository;
import com.artacademy.repository.ArtExhibitionRepository;
import com.artacademy.service.ArtExhibitionService;
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
public class ArtExhibitionServiceImpl implements ArtExhibitionService {

    private final ArtExhibitionRepository artExhibitionRepository;
    private final ArtExhibitionCategoryRepository categoryRepository;
    private final ArtExhibitionMapper artExhibitionMapper;

    @Override
    @Transactional
    public ArtExhibitionResponseDto create(ArtExhibitionRequestDto request) {
        log.info("Creating exhibition: {}", request.getName());
        ArtExhibition entity = artExhibitionMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            ArtExhibitionCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtExhibitionCategory", "id",
                            request.getCategoryId()));
            entity.setArtExhibitionCategory(category);
        }

        return artExhibitionMapper.toDto(artExhibitionRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtExhibitionResponseDto getById(UUID id) {
        ArtExhibition entity = artExhibitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtExhibition", "id", id));
        return artExhibitionMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtExhibitionResponseDto> getAll(Specification<ArtExhibition> spec, Pageable pageable) {
        return artExhibitionRepository.findAll(spec, pageable).map(artExhibitionMapper::toDto);
    }

    @Override
    @Transactional
    public ArtExhibitionResponseDto update(UUID id, ArtExhibitionRequestDto request) {
        log.info("Updating exhibition ID: {}", id);
        ArtExhibition entity = artExhibitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtExhibition", "id", id));

        artExhibitionMapper.updateEntity(request, entity);

        if (request.getCategoryId() != null) {
            ArtExhibitionCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("ArtExhibitionCategory", "id",
                            request.getCategoryId()));
            entity.setArtExhibitionCategory(category);
        }

        return artExhibitionMapper.toDto(artExhibitionRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.warn("Deleting exhibition ID: {}", id);
        if (!artExhibitionRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtExhibition", "id", id);
        }
        artExhibitionRepository.deleteById(id);
    }
}
