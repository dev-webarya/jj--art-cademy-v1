package com.artacademy.specification;

import com.artacademy.entity.Product;
import com.artacademy.entity.StockItem;
import com.artacademy.entity.Store;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class StockItemSpecification {

    public static Specification<StockItem> hasProductId(UUID productId) {
        return (root, query, cb) -> {
            if (productId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("product").get("id"), productId);
        };
    }

    public static Specification<StockItem> hasStoreId(UUID storeId) {
        return (root, query, cb) -> {
            if (storeId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("store").get("id"), storeId);
        };
    }

    public static Specification<StockItem> isCentralWarehouse(Boolean isCentral) {
        return (root, query, cb) -> {
            if (isCentral == null) {
                return cb.conjunction();
            }
            if (isCentral) {
                return cb.isNull(root.get("store"));
            } else {
                return cb.isNotNull(root.get("store"));
            }
        };
    }

    public static Specification<StockItem> hasQuantityLessThan(Integer quantity) {
        return (root, query, cb) -> {
            if (quantity == null) {
                return cb.conjunction();
            }
            return cb.lessThan(root.get("quantity"), quantity);
        };
    }

    public static Specification<StockItem> hasProductSku(String sku) {
        return (root, query, cb) -> {
            if (sku == null || sku.isEmpty()) {
                return cb.conjunction();
            }
            Join<StockItem, Product> productJoin = root.join("product");
            return cb.equal(cb.lower(productJoin.get("sku")), sku.toLowerCase());
        };
    }
}