package com.artacademy.specification;

import com.artacademy.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    // Enable fetching related data in a single query to avoid N+1
    // This is a more advanced pattern but highly recommended.
    public static Specification<Product> fetchAllRelations() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("category", JoinType.LEFT);
                root.fetch("collections", JoinType.LEFT);
                root.fetch("attributes", JoinType.LEFT).fetch("attributeType", JoinType.LEFT);
                root.fetch("images", JoinType.LEFT);
                query.distinct(true);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasSku(String sku) {
        return (root, query, cb) -> {
            if (sku == null || sku.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("sku")), sku.toLowerCase());
        };
    }

    public static Specification<Product> isActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("basePrice"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
            } else if (maxPrice != null) {
                return cb.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Product> inCategory(Integer categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Product> inCollection(Integer collectionId) {
        return (root, query, cb) -> {
            if (collectionId == null) {
                return cb.conjunction();
            }
            Join<Product, Collection> collectionJoin = root.join("collections");
            return cb.equal(collectionJoin.get("id"), collectionId);
        };
    }

    public static Specification<Product> hasAttribute(Integer attributeValueId) {
        return (root, query, cb) -> {
            if (attributeValueId == null) {
                return cb.conjunction();
            }
            Join<Product, AttributeValue> attributeJoin = root.join("attributes");
            return cb.equal(attributeJoin.get("id"), attributeValueId);
        };
    }
}