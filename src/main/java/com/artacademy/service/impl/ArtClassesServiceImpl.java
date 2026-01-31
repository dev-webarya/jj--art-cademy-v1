package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtClassesRequestDto;
import com.artacademy.dto.response.ArtClassesResponseDto;
import com.artacademy.entity.ArtClasses;
import com.artacademy.entity.ArtClassesCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtClassesMapper;
import com.artacademy.repository.ArtClassesCategoryRepository;
import com.artacademy.repository.ArtClassesRepository;
import com.artacademy.service.ArtClassesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtClassesServiceImpl implements ArtClassesService {

    private final ArtClassesRepository artClassesRepository;
    private final ArtClassesCategoryRepository categoryRepository;
    private final ArtClassesMapper artClassesMapper;

    @Override
    @Transactional
    public ArtClassesResponseDto create(ArtClassesRequestDto request) {
        ArtClassesCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        ArtClasses artClass = artClassesMapper.toEntity(request);
        // Mapper sets categoryId; we only need to set the denormalized name manually
        artClass.setCategoryName(category.getName());

        ArtClasses savedClass = artClassesRepository.save(artClass);
        return artClassesMapper.toDto(savedClass);
    }

    @Override
    public ArtClassesResponseDto getById(String id) {
        ArtClasses artClass = artClassesRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClass", "id", id));
        return artClassesMapper.toDto(artClass);
    }

    @Override
    public Page<ArtClassesResponseDto> getAll(Pageable pageable) {
        return artClassesRepository.findByDeletedFalse(pageable)
                .map(artClassesMapper::toDto);
    }

    @Override
    @Transactional
    public ArtClassesResponseDto update(String id, ArtClassesRequestDto request) {
        ArtClasses artClass = artClassesRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClass", "id", id));

        // 1. Capture the old category ID to detect if it changed
        String oldCategoryId = artClass.getCategoryId();

        // 2. Map new values from Request to Entity (this updates categoryId to the new one)
        artClassesMapper.updateEntity(request, artClass);

        // 3. If category changed, fetch new category and update the name
        if (request.getCategoryId() != null && !request.getCategoryId().equals(oldCategoryId)) {
            ArtClassesCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            
            // Note: Mapper already updated categoryId, we just sync the name
            artClass.setCategoryName(category.getName());
        }

        ArtClasses updatedClass = artClassesRepository.save(artClass);
        return artClassesMapper.toDto(updatedClass);
    }

    @Override
    public void delete(String id) {
        ArtClasses artClass = artClassesRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtClass", "id", id));     
        artClass.setDeleted(true);
        artClassesRepository.save(artClass);
        log.info("Soft deleted class with id: {}", id);
    }
}
