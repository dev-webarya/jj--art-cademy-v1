package com.artacademy.dto.response;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class ArtExhibitionResponseDto {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
    private String categoryId;
    private String categoryName;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private Integer artistCount;
    private Integer artworksCount;
    private Instant createdAt;
    private Instant updatedAt;
}
