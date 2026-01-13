package com.artacademy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ArtExhibitionRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private boolean isActive = true;

    private UUID categoryId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Artist count is required")
    @Min(value = 1, message = "At least one artist required")
    private Integer artistCount;

    @NotNull(message = "Artworks count is required")
    @Min(value = 1, message = "At least one artwork required")
    private Integer artworksCount;
}
