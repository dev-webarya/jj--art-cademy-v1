package com.artacademy.controller;

import com.artacademy.dto.request.AttributeTypeRequestDto;
import com.artacademy.dto.response.AttributeTypeResponseDto;
import com.artacademy.entity.AttributeType;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.ManagerAccess;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.AttributeTypeService;
import com.artacademy.specification.AttributeTypeSpecification;
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
@RequestMapping("/api/v1/attribute-types")
@RequiredArgsConstructor
@Slf4j
public class AttributeTypeController {

    private final AttributeTypeService attributeTypeService;

    @PostMapping
    @ManagerAccess
    public ResponseEntity<AttributeTypeResponseDto> createAttributeType(
            @Valid @RequestBody AttributeTypeRequestDto requestDto) {
        log.info("Creating attribute type: {}", requestDto.getName());
        AttributeTypeResponseDto createdDto = attributeTypeService.createAttributeType(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PublicEndpoint
    public ResponseEntity<AttributeTypeResponseDto> getAttributeTypeById(@PathVariable UUID id) {
        AttributeTypeResponseDto dto = attributeTypeService.getAttributeTypeById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PublicEndpoint
    public ResponseEntity<Page<AttributeTypeResponseDto>> getAllAttributeTypes(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        Specification<AttributeType> spec = Specification.where(AttributeTypeSpecification.hasName(name));

        Page<AttributeTypeResponseDto> dtos = attributeTypeService.getAllAttributeTypes(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @ManagerAccess
    public ResponseEntity<AttributeTypeResponseDto> updateAttributeType(@PathVariable UUID id,
            @Valid @RequestBody AttributeTypeRequestDto requestDto) {
        AttributeTypeResponseDto updatedDto = attributeTypeService.updateAttributeType(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteAttributeType(@PathVariable UUID id) {
        log.warn("Deleting attribute type ID: {}", id);
        attributeTypeService.deleteAttributeType(id);
        return ResponseEntity.noContent().build();
    }
}