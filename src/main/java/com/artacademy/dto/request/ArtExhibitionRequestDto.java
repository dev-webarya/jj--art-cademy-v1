package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ArtExhibitionRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private boolean isActive = true;

    private String categoryId;

    private String imageUrl;

    private LocalDate startDate;

    private LocalDate endDate;

    private String location;

    private Integer artistCount;

    private Integer artworksCount;
}
