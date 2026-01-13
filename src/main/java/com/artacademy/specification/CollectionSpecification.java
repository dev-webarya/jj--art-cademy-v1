package com.artacademy.specification;

import com.artacademy.entity.Collection;
import org.springframework.data.jpa.domain.Specification;

public class CollectionSpecification {

    public static Specification<Collection> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}