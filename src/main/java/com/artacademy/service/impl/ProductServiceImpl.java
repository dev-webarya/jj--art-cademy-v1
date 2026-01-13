package com.artacademy.service.impl;

import com.artacademy.dto.request.ProductRequestDto;
import com.artacademy.dto.response.ProductResponseDto;
import com.artacademy.entity.AttributeValue;
import com.artacademy.entity.Category;
import com.artacademy.entity.Collection;
import com.artacademy.entity.Product;
import com.artacademy.exception.DuplicateResourceException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ProductMapper;
import com.artacademy.repository.AttributeValueRepository;
import com.artacademy.repository.CategoryRepository;
import com.artacademy.repository.CollectionRepository;
import com.artacademy.repository.ProductRepository;
import com.artacademy.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CollectionRepository collectionRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        log.info("Creating product with SKU: {}", requestDto.getSku());
        if (skuExists(requestDto.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + requestDto.getSku() + "' already exists.");
        }

        Product product = productMapper.toEntity(requestDto);
        updateProductRelationships(product, requestDto);

        Product savedProduct = productRepository.save(product);
        log.debug("Product created with ID: {}", savedProduct.getId());
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Specification<Product> spec, Pageable pageable) {
        return productRepository.findAll(spec, pageable)
                .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto requestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (!existingProduct.getSku().equalsIgnoreCase(requestDto.getSku()) && skuExists(requestDto.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + requestDto.getSku() + "' already exists.");
        }

        productMapper.updateEntityFromDto(requestDto, existingProduct);
        updateProductRelationships(existingProduct, requestDto);

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        log.warn("Deleting product ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }

    private void updateProductRelationships(Product product, ProductRequestDto dto) {
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        product.getCollections().clear();
        if (dto.getCollectionIds() != null && !dto.getCollectionIds().isEmpty()) {
            Set<Collection> collections = new HashSet<>(collectionRepository.findAllById(dto.getCollectionIds()));
            if (collections.size() != dto.getCollectionIds().size()) {
                throw new ResourceNotFoundException("One or more Collections not found");
            }
            product.setCollections(collections);
        }

        product.getAttributes().clear();
        if (dto.getAttributeValueIds() != null && !dto.getAttributeValueIds().isEmpty()) {
            Set<AttributeValue> attributes = new HashSet<>(
                    attributeValueRepository.findAllById(dto.getAttributeValueIds()));
            if (attributes.size() != dto.getAttributeValueIds().size()) {
                throw new ResourceNotFoundException("One or more AttributeValues not found");
            }
            product.setAttributes(attributes);
        }
    }

    private boolean skuExists(String sku) {
        return productRepository.exists((root, query, cb) -> cb.equal(cb.lower(root.get("sku")), sku.toLowerCase()));
    }
}