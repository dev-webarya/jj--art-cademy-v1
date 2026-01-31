package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtMaterialsRequestDto;
import com.artacademy.dto.response.ArtMaterialsResponseDto;
import com.artacademy.entity.ArtMaterials;
import com.artacademy.entity.ArtMaterialsCategory;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtMaterialsMapper;
import com.artacademy.repository.ArtMaterialsCategoryRepository;
import com.artacademy.repository.ArtMaterialsRepository;
import com.artacademy.service.ArtMaterialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtMaterialsServiceImpl implements ArtMaterialsService {

    private final ArtMaterialsRepository artMaterialsRepository;
    private final ArtMaterialsCategoryRepository categoryRepository;
    private final ArtMaterialsMapper artMaterialsMapper;

    @Override
    @Transactional
    public ArtMaterialsResponseDto create(ArtMaterialsRequestDto request) {
        log.info("Creating new material: {}", request.getName());

        ArtMaterialsCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        ArtMaterials material = artMaterialsMapper.toEntity(request);
        // Mapper sets categoryId; we only need to set the denormalized name manually
        material.setCategoryName(category.getName());
        
        // Default values
        if (material.getDiscount() == null) {
            material.setDiscount(0);
        }

        ArtMaterials savedMaterial = artMaterialsRepository.save(material);
        return artMaterialsMapper.toDto(savedMaterial);
    }

    @Override
    public ArtMaterialsResponseDto getById(String id) {
        ArtMaterials material = artMaterialsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));
        return artMaterialsMapper.toDto(material);
    }

    @Override
    public Page<ArtMaterialsResponseDto> getAll(Pageable pageable) {
        return artMaterialsRepository.findByDeletedFalse(pageable)
                .map(artMaterialsMapper::toDto);
    }

    @Override
    @Transactional
    public ArtMaterialsResponseDto update(String id, ArtMaterialsRequestDto request) {
        ArtMaterials material = artMaterialsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));

        // 1. Capture the old category ID to detect if it changed
        String oldCategoryId = material.getCategoryId();

        // 2. Map new values from Request to Entity (this updates categoryId to the new one)
        artMaterialsMapper.updateEntity(request, material);

        // 3. If category changed, fetch new category and update the name
        if (request.getCategoryId() != null && !request.getCategoryId().equals(oldCategoryId)) {
            ArtMaterialsCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            
            // Note: Mapper already updated categoryId, we just sync the name
            material.setCategoryName(category.getName());
        }

        ArtMaterials updatedMaterial = artMaterialsRepository.save(material);
        return artMaterialsMapper.toDto(updatedMaterial);
    }

    @Override
    public void delete(String id) {
        ArtMaterials material = artMaterialsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", id));
        
        material.setDeleted(true);
        artMaterialsRepository.save(material);
        log.info("Soft deleted material with id: {}", id);
    }
}
