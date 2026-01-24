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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtWorksServiceImpl implements ArtWorksService {

    private final ArtWorksRepository artWorksRepository;
    private final ArtWorksCategoryRepository artWorksCategoryRepository;
    private final ArtWorksMapper artWorksMapper;

    @Override
    @Transactional
    public ArtWorksResponseDto create(ArtWorksRequestDto request) {
        log.info("Creating new artwork: {}", request.getName());

        ArtWorksCategory category = artWorksCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        ArtWorks artWorks = artWorksMapper.toEntity(request);
        // Mapper sets categoryId; we only need to set the denormalized name manually
        artWorks.setCategoryName(category.getName());

        // Defaults
        if (artWorks.getViews() == null) artWorks.setViews(0);
        if (artWorks.getLikes() == null) artWorks.setLikes(0);

        ArtWorks savedArtWorks = artWorksRepository.save(artWorks);
        return artWorksMapper.toDto(savedArtWorks);
    }

    @Override
    public ArtWorksResponseDto getById(String id) {
        ArtWorks artWorks = artWorksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        
        // Trigger view increment
        incrementViews(id);
        
        return artWorksMapper.toDto(artWorks);
    }

    @Override
    public Page<ArtWorksResponseDto> getAll(Pageable pageable) {
        return artWorksRepository.findByDeletedFalse(pageable)
                .map(artWorksMapper::toDto);
    }

    @Override
    @Transactional
    public ArtWorksResponseDto update(String id, ArtWorksRequestDto request) {
        ArtWorks artWorks = artWorksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));

        // 1. Capture the old category ID to detect if it changed
        String oldCategoryId = artWorks.getCategoryId();

        // 2. Map new values from Request to Entity (this updates categoryId to the new one)
        artWorksMapper.updateEntity(request, artWorks);

        // 3. If category changed, fetch new category and update the name
        if (request.getCategoryId() != null && !request.getCategoryId().equals(oldCategoryId)) {
            ArtWorksCategory category = artWorksCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            
            // Note: Mapper already updated categoryId, we just sync the name
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
        
        int currentViews = artWorks.getViews() == null ? 0 : artWorks.getViews();
        artWorks.setViews(currentViews + 1);
        
        return artWorksMapper.toDto(artWorksRepository.save(artWorks));
    }

    @Override
    public ArtWorksResponseDto incrementLikes(String id) {
        ArtWorks artWorks = artWorksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", id));
        
        int currentLikes = artWorks.getLikes() == null ? 0 : artWorks.getLikes();
        artWorks.setLikes(currentLikes + 1);
        
        return artWorksMapper.toDto(artWorksRepository.save(artWorks));
    }
}
