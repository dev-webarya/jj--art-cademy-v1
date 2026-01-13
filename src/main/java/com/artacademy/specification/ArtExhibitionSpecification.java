package com.artacademy.specification;

import com.artacademy.entity.ArtExhibition;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class ArtExhibitionSpecification {

    public static Specification<ArtExhibition> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ArtExhibition> hasLocation(String location) {
        return (root, query, cb) -> location == null ? null
                : cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<ArtExhibition> isActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<ArtExhibition> isOngoing(LocalDate date) {
        return (root, query, cb) -> date == null ? null
                : cb.and(
                        cb.lessThanOrEqualTo(root.get("startDate"), date),
                        cb.greaterThanOrEqualTo(root.get("endDate"), date));
    }

    public static Specification<ArtExhibition> startsAfter(LocalDate date) {
        return (root, query, cb) -> date == null ? null : cb.greaterThanOrEqualTo(root.get("startDate"), date);
    }

    public static Specification<ArtExhibition> inCategory(UUID categoryId) {
        return (root, query, cb) -> categoryId == null ? null
                : cb.equal(root.get("artExhibitionCategory").get("id"), categoryId);
    }
}
