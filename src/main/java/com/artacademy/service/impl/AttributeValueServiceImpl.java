package com.artacademy.service.impl;

import com.artacademy.dto.request.AttributeValueRequestDto;
import com.artacademy.dto.response.AttributeValueResponseDto;
import com.artacademy.entity.AttributeType;
import com.artacademy.entity.AttributeValue;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.AttributeValueMapper;
import com.artacademy.repository.AttributeTypeRepository;
import com.artacademy.repository.AttributeValueRepository;
import com.artacademy.service.AttributeValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttributeValueServiceImpl implements AttributeValueService {

        private final AttributeValueRepository attributeValueRepository;
        private final AttributeTypeRepository attributeTypeRepository; // To link the type
        private final AttributeValueMapper attributeValueMapper;

        @Override
        @Transactional
        public AttributeValueResponseDto createAttributeValue(AttributeValueRequestDto requestDto) {
                log.info("Creating attribute value: {} for type: {}", requestDto.getValue(),
                                requestDto.getAttributeTypeId());
                AttributeType attributeType = attributeTypeRepository.findById(requestDto.getAttributeTypeId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException("AttributeType", "id",
                                                                requestDto.getAttributeTypeId()));

                AttributeValue attributeValue = attributeValueMapper.toEntity(requestDto);
                attributeValue.setAttributeType(attributeType); // Set the relationship

                AttributeValue savedAttributeValue = attributeValueRepository.save(attributeValue);
                return attributeValueMapper.toDto(savedAttributeValue);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<AttributeValueResponseDto> getAllAttributeValues(Specification<AttributeValue> spec,
                        Pageable pageable) {
                return attributeValueRepository.findAll(spec, pageable)
                                .map(attributeValueMapper::toDto);
        }

        @Override
        @Transactional(readOnly = true)
        public AttributeValueResponseDto getAttributeValueById(UUID id) {
                return attributeValueRepository.findById(id)
                                .map(attributeValueMapper::toDto)
                                .orElseThrow(() -> new ResourceNotFoundException("AttributeValue", "id", id));
        }

        @Override
        @Transactional
        public AttributeValueResponseDto updateAttributeValue(UUID id, AttributeValueRequestDto requestDto) {
                AttributeValue existingValue = attributeValueRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("AttributeValue", "id", id));

                // Find the parent AttributeType
                AttributeType attributeType = attributeTypeRepository.findById(requestDto.getAttributeTypeId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException("AttributeType", "id",
                                                                requestDto.getAttributeTypeId()));

                // Update basic fields
                attributeValueMapper.updateEntityFromDto(requestDto, existingValue);
                existingValue.setAttributeType(attributeType); // Update the relationship

                AttributeValue updatedValue = attributeValueRepository.save(existingValue);
                return attributeValueMapper.toDto(updatedValue);
        }

        @Override
        @Transactional
        public void deleteAttributeValue(UUID id) {
                log.warn("Deleting attribute value ID: {}", id);
                AttributeValue attributeValue = attributeValueRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("AttributeValue", "id", id));
                attributeValueRepository.delete(attributeValue);
        }
}