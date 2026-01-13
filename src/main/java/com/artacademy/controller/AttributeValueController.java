package com.artacademy.controller;

import com.artacademy.dto.request.AttributeValueRequestDto;
import com.artacademy.dto.response.AttributeValueResponseDto;
import com.artacademy.entity.AttributeValue;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.AttributeValueService;
import com.artacademy.specification.AttributeValueSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attribute-values")
@RequiredArgsConstructor
@Slf4j
public class AttributeValueController {

    private final AttributeValueService attributeValueService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<AttributeValueResponseDto> createAttributeValue(
            @Valid @RequestBody AttributeValueRequestDto requestDto) {
        log.info("Creating attribute value: {} for type: {}", requestDto.getValue(), requestDto.getAttributeTypeId());
        AttributeValueResponseDto createdDto = attributeValueService.createAttributeValue(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<AttributeValueResponseDto> getAttributeValueById(@PathVariable UUID id) {
        AttributeValueResponseDto dto = attributeValueService.getAttributeValueById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<AttributeValueResponseDto>> getAllAttributeValues(
            @RequestParam(required = false) String value,
            @RequestParam(required = false) UUID attributeTypeId,
            @RequestParam(required = false) String attributeTypeName,
            Pageable pageable) {

        Specification<AttributeValue> spec = Specification.where(AttributeValueSpecification.hasValue(value))
                .and(AttributeValueSpecification.hasAttributeTypeId(attributeTypeId))
                .and(AttributeValueSpecification.hasAttributeTypeName(attributeTypeName));

        Page<AttributeValueResponseDto> dtos = attributeValueService.getAllAttributeValues(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<AttributeValueResponseDto> updateAttributeValue(@PathVariable UUID id,
            @Valid @RequestBody AttributeValueRequestDto requestDto) {
        AttributeValueResponseDto updatedDto = attributeValueService.updateAttributeValue(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable UUID id) {
        log.warn("Deleting attribute value ID: {}", id);
        attributeValueService.deleteAttributeValue(id);
        return ResponseEntity.noContent().build();
    }
}