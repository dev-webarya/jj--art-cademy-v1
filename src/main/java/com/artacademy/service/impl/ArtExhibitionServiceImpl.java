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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtExhibitionServiceImpl implements ArtExhibitionService {

    private final ArtExhibitionRepository artExhibitionRepository;
    private final ArtExhibitionCategoryRepository categoryRepository;
    private final ArtExhibitionMapper artExhibitionMapper;

    @Override
    public ArtExhibitionResponseDto create(ArtExhibitionRequestDto request) {
        ArtExhibitionCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        ArtExhibition exhibition = artExhibitionMapper.toEntity(request);
        exhibition.setCategoryId(category.getId());
        exhibition.setCategoryName(category.getName());

        // Defaults
        if (exhibition.getArtistCount() == null)
            exhibition.setArtistCount(0);
        if (exhibition.getArtworksCount() == null)
            exhibition.setArtworksCount(0);

        ArtExhibition savedExhibition = artExhibitionRepository.save(exhibition);
        return artExhibitionMapper.toDto(savedExhibition);
    }

    @Override
    public ArtExhibitionResponseDto getById(String id) {
        ArtExhibition exhibition = artExhibitionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exhibition", "id", id));
        return artExhibitionMapper.toDto(exhibition);
    }

    @Override
    public Page<ArtExhibitionResponseDto> getAll(Pageable pageable) {
        return artExhibitionRepository.findByDeletedFalse(pageable)
                .map(artExhibitionMapper::toDto);
    }

    @Override
    public ArtExhibitionResponseDto update(String id, ArtExhibitionRequestDto request) {
        ArtExhibition exhibition = artExhibitionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exhibition", "id", id));

        artExhibitionMapper.updateEntity(request, exhibition);

        if (request.getCategoryId() != null && !request.getCategoryId().equals(exhibition.getCategoryId())) {
            ArtExhibitionCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            exhibition.setCategoryId(category.getId());
            exhibition.setCategoryName(category.getName());
        }

        ArtExhibition updatedExhibition = artExhibitionRepository.save(exhibition);
        return artExhibitionMapper.toDto(updatedExhibition);
    }

    @Override
    public void delete(String id) {
        ArtExhibition exhibition = artExhibitionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exhibition", "id", id));
        exhibition.setDeleted(true);
        artExhibitionRepository.save(exhibition);
        log.info("Soft deleted exhibition with id: {}", id);
    }
}
