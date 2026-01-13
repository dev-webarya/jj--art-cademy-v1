package com.artacademy.service.impl;

import com.artacademy.dto.request.ProductImageRequestDto;
import com.artacademy.dto.response.ProductImageResponseDto;
import com.artacademy.entity.Product;
import com.artacademy.entity.ProductImage;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ProductImageMapper;
import com.artacademy.repository.ProductImageRepository;
import com.artacademy.repository.ProductRepository;
import com.artacademy.service.ProductImageService;
import com.artacademy.specification.ProductImageSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    @Transactional
    public ProductImageResponseDto addImageToProduct(ProductImageRequestDto requestDto) {
        log.info("Adding image to product: {}", requestDto.getProductId());
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", requestDto.getProductId()));

        // If this new image is set as primary, reset all others for this product
        if (requestDto.isPrimary()) {
            productImageRepository.resetAllPrimaryImagesForProduct(product.getId());
        }

        ProductImage productImage = productImageMapper.toEntity(requestDto);
        productImage.setProduct(product);

        ProductImage savedImage = productImageRepository.save(productImage);
        return productImageMapper.toDto(savedImage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductImageResponseDto> getImagesForProduct(UUID productId, Pageable pageable) {
        Specification<ProductImage> spec = Specification.where(ProductImageSpecification.hasProductId(productId));
        return productImageRepository.findAll(spec, pageable)
                .map(productImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageResponseDto getImageById(UUID id) {
        return productImageRepository.findById(id)
                .map(productImageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));
    }

    @Override
    @Transactional
    public ProductImageResponseDto updateImageDetails(UUID id, ProductImageRequestDto requestDto) {
        ProductImage existingImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));

        // Check if product ID is changing (not usually allowed, but good to check)
        if (!existingImage.getProduct().getId().equals(requestDto.getProductId())) {
            throw new IllegalArgumentException("Cannot change the product of an existing image.");
        }

        // If this image is being set to primary, reset others.
        if (requestDto.isPrimary() && !existingImage.isPrimary()) {
            productImageRepository.resetAllPrimaryImagesForProduct(requestDto.getProductId());
        }

        productImageMapper.updateEntityFromDto(requestDto, existingImage);

        ProductImage updatedImage = productImageRepository.save(existingImage);
        return productImageMapper.toDto(updatedImage);
    }

    @Override
    @Transactional
    public void deleteImage(UUID id) {
        log.warn("Deleting product image ID: {}", id);
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));
        productImageRepository.delete(image);
    }
}