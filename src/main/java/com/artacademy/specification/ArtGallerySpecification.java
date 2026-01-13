package com.artacademy.specification;

import com.artacademy.entity.ArtGallery;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ArtGallerySpecification {

    public static Specification<ArtGallery> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ArtGallery> isActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<ArtGallery> inCategory(UUID categoryId) {
        return (root, query, cb) -> categoryId == null ? null
                : cb.equal(root.get("artGalleryCategory").get("id"), categoryId);
    }
}
