package com.artacademy.service;

import com.artacademy.dto.request.ProductImageRequestDto;
import com.artacademy.dto.response.ProductImageResponseDto;
import com.artacademy.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public interface ProductImageService {

    ProductImageResponseDto addImageToProduct(ProductImageRequestDto requestDto);

    Page<ProductImageResponseDto> getImagesForProduct(UUID productId, Pageable pageable);

    ProductImageResponseDto getImageById(UUID id);

    ProductImageResponseDto updateImageDetails(UUID id, ProductImageRequestDto requestDto);

    void deleteImage(UUID id);
}