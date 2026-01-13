package com.artacademy.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class AttributeTypeResponseDto {
    private UUID id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    // We don't include the values here to avoid circular dependencies in DTOs.
    // A separate endpoint to get values for an attribute type is better.
}