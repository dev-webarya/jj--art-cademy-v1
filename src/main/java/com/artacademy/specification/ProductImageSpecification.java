package com.artacademy.specification;

import com.artacademy.entity.ProductImage;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class ProductImageSpecification {

    public static Specification<ProductImage> hasProductId(UUID productId) {
        return (root, query, cb) -> {
            if (productId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("product").get("id"), productId);
        };
    }

    public static Specification<ProductImage> isPrimary(Boolean isPrimary) {
        return (root, query, cb) -> {
            if (isPrimary == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isPrimary"), isPrimary);
        };
    }
}