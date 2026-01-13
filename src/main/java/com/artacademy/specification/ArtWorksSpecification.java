package com.artacademy.specification;

import com.artacademy.entity.ArtWorks;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class ArtWorksSpecification {

    public static Specification<ArtWorks> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ArtWorks> hasArtistName(String artistName) {
        return (root, query, cb) -> artistName == null ? null
                : cb.like(cb.lower(root.get("artistName")), "%" + artistName.toLowerCase() + "%");
    }

    public static Specification<ArtWorks> hasArtMedium(String artMedium) {
        return (root, query, cb) -> artMedium == null ? null
                : cb.like(cb.lower(root.get("artMedium")), "%" + artMedium.toLowerCase() + "%");
    }

    public static Specification<ArtWorks> isActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<ArtWorks> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
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

    public static Specification<ArtWorks> inCategory(UUID categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }
}
