package com.artacademy.service;

import com.artacademy.dto.request.AttributeTypeRequestDto;
import com.artacademy.dto.response.AttributeTypeResponseDto;
import com.artacademy.entity.AttributeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface AttributeTypeService {

    AttributeTypeResponseDto createAttributeType(AttributeTypeRequestDto requestDto);

    Page<AttributeTypeResponseDto> getAllAttributeTypes(Specification<AttributeType> spec, Pageable pageable);

    AttributeTypeResponseDto getAttributeTypeById(UUID id);

    AttributeTypeResponseDto updateAttributeType(UUID id, AttributeTypeRequestDto requestDto);

    void deleteAttributeType(UUID id);
}