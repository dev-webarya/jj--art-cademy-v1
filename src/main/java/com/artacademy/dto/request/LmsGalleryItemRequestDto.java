package com.artacademy.dto.request;

import com.artacademy.enums.LmsMediaType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for uploading gallery items.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsGalleryItemRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Media URL is required")
    private String mediaUrl;

    private LmsMediaType mediaType;

    private String thumbnailUrl;

    private String classId;

    private List<String> tags;
}
