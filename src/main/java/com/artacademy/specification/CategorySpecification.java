package com.artacademy.specification;

import com.artacademy.entity.Category;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CategorySpecification {

    /**
     * Creates a Specification to filter by category name (case-insensitive, partial
     * match).
     */
    public static Specification<Category> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction(); // No filter
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * Creates a Specification to filter categories that are root (parent is null).
     */
    public static Specification<Category> isRootCategory() {
        return (root, query, cb) -> cb.isNull(root.get("parent"));
    }

    /**
     * Creates a Specification to filter by a specific parent ID.
     */
    public static Specification<Category> hasParentId(UUID parentId) {
        return (root, query, cb) -> {
            if (parentId == null) {
                return cb.conjunction(); // No filter
            }
            return cb.equal(root.get("parent").get("id"), parentId);
        };
    }
}