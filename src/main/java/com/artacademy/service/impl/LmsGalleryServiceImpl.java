package com.artacademy.service.impl;

import com.artacademy.dto.request.LmsGalleryItemRequestDto;
import com.artacademy.dto.request.LmsGalleryVerifyRequestDto;
import com.artacademy.dto.response.LmsGalleryItemResponseDto;
import com.artacademy.entity.ArtClasses;
import com.artacademy.entity.LmsGalleryItem;
import com.artacademy.entity.User;
import com.artacademy.enums.VerificationStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.LmsGalleryItemMapper;
import com.artacademy.repository.ArtClassesRepository;
import com.artacademy.repository.LmsGalleryItemRepository;
import com.artacademy.service.LmsGalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LmsGalleryServiceImpl implements LmsGalleryService {

    private final LmsGalleryItemRepository galleryRepository;
    private final ArtClassesRepository artClassesRepository;
    private final LmsGalleryItemMapper galleryMapper;

    @Override
    @Transactional
    public LmsGalleryItemResponseDto create(LmsGalleryItemRequestDto request) {
        User currentUser = getCurrentUser();

        LmsGalleryItem item = galleryMapper.toEntity(request);
        item.setUploadedBy(currentUser.getId());
        item.setUploaderName(currentUser.getFirstName() + " " + currentUser.getLastName());
        item.setUploaderRole(currentUser.getRoles().isEmpty() ? "USER" : currentUser.getRoles().iterator().next());
        item.setVerificationStatus(VerificationStatus.PENDING);
        item.setIsPublic(false);
        item.setIsFeatured(false);
        item.setViewCount(0);
        item.setLikeCount(0);

        // Set class name if classId is provided
        if (request.getClassId() != null) {
            artClassesRepository.findById(request.getClassId())
                    .ifPresent(artClass -> item.setClassName(artClass.getName()));
        }

        return galleryMapper.toResponse(galleryRepository.save(item));
    }

    @Override
    public LmsGalleryItemResponseDto getById(String id) {
        return galleryMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public LmsGalleryItemResponseDto update(String id, LmsGalleryItemRequestDto request) {
        LmsGalleryItem item = findById(id);
        galleryMapper.updateEntity(request, item);

        // Update class name if classId changed
        if (request.getClassId() != null) {
            artClassesRepository.findById(request.getClassId())
                    .ifPresent(artClass -> item.setClassName(artClass.getName()));
        }

        return galleryMapper.toResponse(galleryRepository.save(item));
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!galleryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gallery item not found: " + id);
        }
        galleryRepository.deleteById(id);
    }

    @Override
    public Page<LmsGalleryItemResponseDto> getAll(Pageable pageable) {
        return galleryRepository.findAll(pageable).map(galleryMapper::toResponse);
    }

    @Override
    public Page<LmsGalleryItemResponseDto> getByUploader(String userId, Pageable pageable) {
        return galleryRepository.findByUploadedBy(userId, pageable).map(galleryMapper::toResponse);
    }

    @Override
    public Page<LmsGalleryItemResponseDto> getByStatus(VerificationStatus status, Pageable pageable) {
        return galleryRepository.findByVerificationStatus(status, pageable).map(galleryMapper::toResponse);
    }

    @Override
    public Page<LmsGalleryItemResponseDto> getPublicItems(Pageable pageable) {
        return galleryRepository.findByIsPublicTrue(pageable).map(galleryMapper::toResponse);
    }

    @Override
    public Page<LmsGalleryItemResponseDto> getFeaturedItems(Pageable pageable) {
        return galleryRepository.findByIsFeaturedTrue(pageable).map(galleryMapper::toResponse);
    }

    @Override
    @Transactional
    public LmsGalleryItemResponseDto verify(String id, LmsGalleryVerifyRequestDto request) {
        LmsGalleryItem item = findById(id);
        User admin = getCurrentUser();

        if (request.getStatus() == VerificationStatus.APPROVED) {
            item.approve(admin.getId(), admin.getFirstName() + " " + admin.getLastName());
        } else if (request.getStatus() == VerificationStatus.REJECTED) {
            item.reject(admin.getId(), admin.getFirstName() + " " + admin.getLastName(),
                    request.getRejectionReason());
        }

        if (request.getIsFeatured() != null) {
            item.setIsFeatured(request.getIsFeatured());
        }

        return galleryMapper.toResponse(galleryRepository.save(item));
    }

    @Override
    public long countPending() {
        return galleryRepository.countByVerificationStatus(VerificationStatus.PENDING);
    }

    private LmsGalleryItem findById(String id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery item not found: " + id));
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
