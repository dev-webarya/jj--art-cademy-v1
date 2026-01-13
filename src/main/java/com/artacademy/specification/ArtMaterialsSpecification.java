package com.artacademy.specification;

import com.artacademy.entity.ArtMaterials;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class ArtMaterialsSpecification {

    public static Specification<ArtMaterials> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ArtMaterials> isActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<ArtMaterials> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null)
                return null;
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("basePrice"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
            }
            return cb.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
        };
    }

    public static Specification<ArtMaterials> hasStock(Boolean inStock) {
        return (root, query, cb) -> {
            if (inStock == null)
                return null;
            if (inStock) {
                return cb.greaterThan(root.get("stock"), BigDecimal.ZERO);
            }
            return cb.lessThanOrEqualTo(root.get("stock"), BigDecimal.ZERO);
        };
    }

    public static Specification<ArtMaterials> inCategory(UUID categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }
}
