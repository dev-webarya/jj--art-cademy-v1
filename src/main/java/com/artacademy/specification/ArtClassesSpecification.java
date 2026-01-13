package com.artacademy.specification;

import com.artacademy.entity.ArtClasses;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class ArtClassesSpecification {

    public static Specification<ArtClasses> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ArtClasses> hasProficiency(String proficiency) {
        return (root, query, cb) -> proficiency == null ? null
                : cb.equal(cb.lower(root.get("proficiency")), proficiency.toLowerCase());
    }

    public static Specification<ArtClasses> isActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<ArtClasses> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
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

    public static Specification<ArtClasses> inCategory(UUID categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }
}
