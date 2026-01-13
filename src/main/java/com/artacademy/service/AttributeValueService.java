package com.artacademy.service;

import com.artacademy.dto.request.AttributeValueRequestDto;
import com.artacademy.dto.response.AttributeValueResponseDto;
import com.artacademy.entity.AttributeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface AttributeValueService {

    AttributeValueResponseDto createAttributeValue(AttributeValueRequestDto requestDto);

    Page<AttributeValueResponseDto> getAllAttributeValues(Specification<AttributeValue> spec, Pageable pageable);

    AttributeValueResponseDto getAttributeValueById(UUID id);

    AttributeValueResponseDto updateAttributeValue(UUID id, AttributeValueRequestDto requestDto);

    void deleteAttributeValue(UUID id);
}