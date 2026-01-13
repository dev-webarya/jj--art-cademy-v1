package com.artacademy.specification;

import com.artacademy.entity.AttributeType;
import org.springframework.data.jpa.domain.Specification;

public class AttributeTypeSpecification {

    public static Specification<AttributeType> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}