package com.artacademy.service.impl;

import com.artacademy.dto.request.AttributeTypeRequestDto;
import com.artacademy.dto.response.AttributeTypeResponseDto;
import com.artacademy.entity.AttributeType;
import com.artacademy.exception.DuplicateResourceException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.AttributeTypeMapper;
import com.artacademy.repository.AttributeTypeRepository;
import com.artacademy.service.AttributeTypeService;
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
public class AttributeTypeServiceImpl implements AttributeTypeService {

    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeTypeMapper attributeTypeMapper;

    @Override
    @Transactional
    public AttributeTypeResponseDto createAttributeType(AttributeTypeRequestDto requestDto) {
        log.info("Creating attribute type: {}", requestDto.getName());
        if (attributeTypeNameExists(requestDto.getName())) {
            throw new DuplicateResourceException(
                    "Attribute Type with name '" + requestDto.getName() + "' already exists.");
        }

        AttributeType attributeType = attributeTypeMapper.toEntity(requestDto);
        AttributeType savedAttributeType = attributeTypeRepository.save(attributeType);
        return attributeTypeMapper.toDto(savedAttributeType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttributeTypeResponseDto> getAllAttributeTypes(Specification<AttributeType> spec, Pageable pageable) {
        return attributeTypeRepository.findAll(spec, pageable)
                .map(attributeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AttributeTypeResponseDto getAttributeTypeById(UUID id) {
        return attributeTypeRepository.findById(id)
                .map(attributeTypeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("AttributeType", "id", id));
    }

    @Override
    @Transactional
    public AttributeTypeResponseDto updateAttributeType(UUID id, AttributeTypeRequestDto requestDto) {
        AttributeType existingAttributeType = attributeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttributeType", "id", id));

        if (!existingAttributeType.getName().equalsIgnoreCase(requestDto.getName())
                && attributeTypeNameExists(requestDto.getName())) {
            throw new DuplicateResourceException(
                    "Attribute Type with name '" + requestDto.getName() + "' already exists.");
        }

        attributeTypeMapper.updateEntityFromDto(requestDto, existingAttributeType);

        AttributeType updatedAttributeType = attributeTypeRepository.save(existingAttributeType);
        return attributeTypeMapper.toDto(updatedAttributeType);
    }

    @Override
    @Transactional
    public void deleteAttributeType(UUID id) {
        log.warn("Deleting attribute type ID: {}", id);
        AttributeType attributeType = attributeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttributeType", "id", id));

        // Prevent deletion if values exist (which implies products might use them)
        if (!attributeType.getValues().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Cannot delete Attribute Type. It has associated values (e.g., 'Gold', 'Silver'). Delete values first.");
        }

        attributeTypeRepository.delete(attributeType);
    }

    private boolean attributeTypeNameExists(String name) {
        return attributeTypeRepository
                .exists((root, query, cb) -> cb.equal(cb.lower(root.get("name")), name.toLowerCase()));
    }
}