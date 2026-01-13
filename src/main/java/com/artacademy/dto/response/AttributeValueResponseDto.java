package com.artacademy.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class AttributeValueResponseDto {
    private UUID id;
    private UUID attributeTypeId;
    private String attributeTypeName; // Include name for context
    private String value;
    private Instant createdAt;
    private Instant updatedAt;
}