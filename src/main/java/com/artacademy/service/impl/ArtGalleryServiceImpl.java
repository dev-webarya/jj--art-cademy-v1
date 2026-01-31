package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.entity.ArtGallery;
import com.artacademy.entity.ArtGalleryCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtGalleryMapper;
import com.artacademy.repository.ArtGalleryCategoryRepository;
import com.artacademy.repository.ArtGalleryRepository;
import com.artacademy.service.ArtGalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtGalleryServiceImpl implements ArtGalleryService {

    private final ArtGalleryRepository artGalleryRepository;
    private final ArtGalleryCategoryRepository categoryRepository;
    private final ArtGalleryMapper artGalleryMapper;

    @Override
    @Transactional
    public ArtGalleryResponseDto create(ArtGalleryRequestDto request) {
        ArtGalleryCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        ArtGallery gallery = artGalleryMapper.toEntity(request);
        // Mapper sets categoryId; we only need to set the denormalized name manually
        gallery.setCategoryName(category.getName());

        ArtGallery savedGallery = artGalleryRepository.save(gallery);
        return artGalleryMapper.toDto(savedGallery);
    }

    @Override
    public ArtGalleryResponseDto getById(String id) {
        ArtGallery gallery = artGalleryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));
        return artGalleryMapper.toDto(gallery);
    }

    @Override
    public Page<ArtGalleryResponseDto> getAll(Pageable pageable) {
        return artGalleryRepository.findByDeletedFalse(pageable)
                .map(artGalleryMapper::toDto);
    }

    @Override
    @Transactional
    public ArtGalleryResponseDto update(String id, ArtGalleryRequestDto request) {
        ArtGallery gallery = artGalleryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));

        // 1. Capture the old category ID to detect if it changed
        String oldCategoryId = gallery.getCategoryId();

        // 2. Map new values from Request to Entity (this updates categoryId to the new
        // one)
        artGalleryMapper.updateEntity(request, gallery);

        // 3. If category changed, fetch new category and update the name
        if (request.getCategoryId() != null && !request.getCategoryId().equals(oldCategoryId)) {
            ArtGalleryCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

            // Note: Mapper already updated categoryId, we just sync the name
            gallery.setCategoryName(category.getName());
        }

        ArtGallery updatedGallery = artGalleryRepository.save(gallery);
        return artGalleryMapper.toDto(updatedGallery);
    }

    @Override
    public void delete(String id) {
        ArtGallery gallery = artGalleryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));

        gallery.setDeleted(true);
        artGalleryRepository.save(gallery);
        log.info("Soft deleted gallery with id: {}", id);
    }
}
