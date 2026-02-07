package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtGalleryRequestDto;
import com.artacademy.dto.response.ArtGalleryResponseDto;
import com.artacademy.entity.ArtGallery;
import com.artacademy.entity.ArtGalleryCategory;
import com.artacademy.enums.VerificationStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtGalleryMapper;
import com.artacademy.repository.ArtGalleryCategoryRepository;
import com.artacademy.repository.ArtGalleryRepository;
import com.artacademy.service.ArtGalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        gallery.setCategoryName(category.getName());

        // Get current user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        gallery.setUserId(userId);
        gallery.setUserName(userId);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // If Admin, APPROVED by default. If Customer, PENDING.
        if (isAdmin) {
            gallery.setStatus(VerificationStatus.APPROVED);
        } else {
            gallery.setStatus(VerificationStatus.PENDING);
        }

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return artGalleryRepository.findByDeletedFalse(pageable)
                    .map(artGalleryMapper::toDto);
        } else {
            return artGalleryRepository.findByStatusAndDeletedFalse(VerificationStatus.APPROVED, pageable)
                    .map(artGalleryMapper::toDto);
        }
    }

    @Override
    public ArtGalleryResponseDto verifyGallery(String id, VerificationStatus status) {
        ArtGallery gallery = artGalleryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtGallery", "id", id));

        gallery.setStatus(status);
        ArtGallery savedGallery = artGalleryRepository.save(gallery);
        return artGalleryMapper.toDto(savedGallery);
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

    @Override
    public Page<ArtGalleryResponseDto> getMyGalleries(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return artGalleryRepository.findByUserIdAndDeletedFalse(userId, pageable)
                .map(artGalleryMapper::toDto);
    }

    @Override
    public Page<ArtGalleryResponseDto> getMyGalleriesByStatus(VerificationStatus status, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return artGalleryRepository.findByUserIdAndStatusAndDeletedFalse(userId, status, pageable)
                .map(artGalleryMapper::toDto);
    }
}
