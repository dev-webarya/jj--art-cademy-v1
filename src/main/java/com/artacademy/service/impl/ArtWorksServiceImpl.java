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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtWorksServiceImpl implements ArtWorksService {

    private final ArtWorksRepository artWorksRepository;
    private final ArtWorksCategoryRepository artWorksCategoryRepository;
    private final ArtWorksMapper artWorksMapper;

    @Override
    public ArtWorksResponseDto create(ArtWorksRequestDto request) {
        log.info("Creating new artwork: {}", request.getName());

        // Validate Category
        ArtWorksCategory category = artWorksCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        ArtWorks artWorks = artWorksMapper.toEntity(request);
        // Explicitly set category ID and name (denormalization for MongoDB)
        artWorks.setCategoryId(category.getId());
        artWorks.setCategoryName(category.getName());

        ArtWorks savedArtWorks = artWorksRepository.save(artWorks);
        return artWorksMapper.toDto(savedArtWorks);
    }

    @Override
    public ArtWorksResponseDto getById(String id) {
        ArtWorks artWorks = artWorksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));

        // Increment views async (fire and forget logic in real app, here sync)
        incrementViews(id);

        return artWorksMapper.toDto(artWorks);
    }

    @Override
    public Page<ArtWorksResponseDto> getAll(Pageable pageable) {
        return artWorksRepository.findByDeletedFalse(pageable)
                .map(artWorksMapper::toDto);
    }

    @Override
    public ArtWorksResponseDto update(String id, ArtWorksRequestDto request) {
        ArtWorks artWorks = artWorksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));

        // Start updates
        artWorksMapper.updateEntity(request, artWorks);

        // Update Category if changed
        if (request.getCategoryId() != null && !request.getCategoryId().equals(artWorks.getCategoryId())) {
            ArtWorksCategory category = artWorksCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            artWorks.setCategoryId(category.getId());
            artWorks.setCategoryName(category.getName());
        }

        ArtWorks updatedArtWorks = artWorksRepository.save(artWorks);
        return artWorksMapper.toDto(updatedArtWorks);
    }

    @Override
    public void delete(String id) {
        ArtWorks artWorks = artWorksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        artWorks.setDeleted(true);
        artWorksRepository.save(artWorks);
        log.info("Soft deleted artwork with id: {}", id);
    }

    @Override
    public ArtWorksResponseDto incrementViews(String id) {
        ArtWorks artWorks = artWorksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        artWorks.setViews(artWorks.getViews() == null ? 1 : artWorks.getViews() + 1);
        return artWorksMapper.toDto(artWorksRepository.save(artWorks));
    }

    @Override
    public ArtWorksResponseDto incrementLikes(String id) {
        ArtWorks artWorks = artWorksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        artWorks.setLikes(artWorks.getLikes() == null ? 1 : artWorks.getLikes() + 1);
        return artWorksMapper.toDto(artWorksRepository.save(artWorks));
    }
}
