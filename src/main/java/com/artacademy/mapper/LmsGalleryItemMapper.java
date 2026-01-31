package com.artacademy.mapper;

import com.artacademy.dto.request.LmsGalleryItemRequestDto;
import com.artacademy.dto.response.LmsGalleryItemResponseDto;
import com.artacademy.entity.LmsGalleryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LmsGalleryItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "uploaderName", ignore = true)
    @Mapping(target = "uploaderRole", ignore = true)
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "verifiedByName", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "isPublic", ignore = true)
    @Mapping(target = "isFeatured", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LmsGalleryItem toEntity(LmsGalleryItemRequestDto dto);

    LmsGalleryItemResponseDto toResponse(LmsGalleryItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "uploaderName", ignore = true)
    @Mapping(target = "uploaderRole", ignore = true)
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "batchId", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "verifiedByName", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "isPublic", ignore = true)
    @Mapping(target = "isFeatured", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(LmsGalleryItemRequestDto dto, @MappingTarget LmsGalleryItem entity);
}
