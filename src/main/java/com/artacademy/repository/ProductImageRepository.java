package com.artacademy.repository;

import com.artacademy.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository
        extends JpaRepository<ProductImage, UUID>, JpaSpecificationExecutor<ProductImage> {

    // Find the current primary image for a product
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(UUID productId);

    // Set all images for a product to be non-primary
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.isPrimary = false WHERE pi.product.id = :productId")
    void resetAllPrimaryImagesForProduct(UUID productId);
}