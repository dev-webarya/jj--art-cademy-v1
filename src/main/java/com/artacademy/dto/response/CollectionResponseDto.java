package com.artacademy.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class CollectionResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}