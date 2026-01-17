package com.artacademy.repository;

import com.artacademy.entity.ArtOrder;
import com.artacademy.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtOrderRepository extends MongoRepository<ArtOrder, String> {

    Optional<ArtOrder> findByOrderNumber(String orderNumber);

    Page<ArtOrder> findByUserId(String userId, Pageable pageable);

    List<ArtOrder> findByUserIdAndStatus(String userId, OrderStatus status);

    Page<ArtOrder> findByStatus(OrderStatus status, Pageable pageable);

    // Query for filtering orders with multiple criteria
    @Query("{ " +
            "$and: [" +
            "  { $or: [ { 'status': ?0 }, { ?0: null } ] }," +
            "  { $or: [ { 'userId': ?1 }, { ?1: null } ] }," +
            "  { $or: [ { 'totalPrice': { $gte: ?2 } }, { ?2: null } ] }," +
            "  { $or: [ { 'totalPrice': { $lte: ?3 } }, { ?3: null } ] }," +
            "  { $or: [ { 'createdAt': { $gte: ?4 } }, { ?4: null } ] }," +
            "  { $or: [ { 'createdAt': { $lte: ?5 } }, { ?5: null } ] }" +
            "]" +
            "}")
    Page<ArtOrder> findWithFilters(
            OrderStatus status,
            String userId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Instant startDate,
            Instant endDate,
            Pageable pageable);
}
