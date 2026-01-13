package com.artacademy.service.impl;

import com.artacademy.dto.request.CollectionRequestDto;
import com.artacademy.dto.response.CollectionResponseDto;
import com.artacademy.entity.Collection;
import com.artacademy.exception.DuplicateResourceException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.CollectionMapper;
import com.artacademy.repository.CollectionRepository;
import com.artacademy.service.CollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;

    @Override
    @Transactional
    public CollectionResponseDto createCollection(CollectionRequestDto requestDto) {
        log.info("Creating collection: {}", requestDto.getName());
        if (collectionNameExists(requestDto.getName())) {
            throw new DuplicateResourceException("Collection with name '" + requestDto.getName() + "' already exists.");
        }

        Collection collection = collectionMapper.toEntity(requestDto);
        Collection savedCollection = collectionRepository.save(collection);
        return collectionMapper.toDto(savedCollection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollectionResponseDto> getAllCollections(Specification<Collection> spec, Pageable pageable) {
        return collectionRepository.findAll(spec, pageable)
                .map(collectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionResponseDto getCollectionById(UUID id) {
        return collectionRepository.findById(id)
                .map(collectionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", id));
    }

    @Override
    @Transactional
    public CollectionResponseDto updateCollection(UUID id, CollectionRequestDto requestDto) {
        Collection existingCollection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", id));

        if (!existingCollection.getName().equalsIgnoreCase(requestDto.getName())
                && collectionNameExists(requestDto.getName())) {
            throw new DuplicateResourceException("Collection with name '" + requestDto.getName() + "' already exists.");
        }

        collectionMapper.updateEntityFromDto(requestDto, existingCollection);

        Collection updatedCollection = collectionRepository.save(existingCollection);
        return collectionMapper.toDto(updatedCollection);
    }

    @Override
    @Transactional
    public void deleteCollection(UUID id) {
        log.warn("Deleting collection ID: {}", id);
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", id));

        if (!collection.getProducts().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Cannot delete collection. There are products associated with it.");
        }

        collectionRepository.delete(collection);
    }

    private boolean collectionNameExists(String name) {
        return collectionRepository
                .exists((root, query, cb) -> cb.equal(cb.lower(root.get("name")), name.toLowerCase()));
    }
}