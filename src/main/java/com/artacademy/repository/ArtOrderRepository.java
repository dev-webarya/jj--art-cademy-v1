package com.artacademy.repository;

import com.artacademy.entity.ArtOrder;
import com.artacademy.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtOrderRepository extends MongoRepository<ArtOrder, String> {

    Optional<ArtOrder> findByOrderNumber(String orderNumber);

    @Query("{'user.userId': ?0, 'deleted': false}")
    List<ArtOrder> findByUserId(String userId);

    @Query("{'user.userId': ?0, 'deleted': false}")
    Page<ArtOrder> findByUserId(String userId, Pageable pageable);

    @Query("{'status': ?0, 'deleted': false}")
    List<ArtOrder> findByStatus(OrderStatus status);

    @Query("{'user.userId': ?0, 'status': ?1, 'deleted': false}")
    List<ArtOrder> findByUserIdAndStatus(String userId, OrderStatus status);

    @Query("{'createdAt': {$gte: ?0, $lte: ?1}, 'deleted': false}")
    List<ArtOrder> findByCreatedAtBetween(Instant startDate, Instant endDate);

    List<ArtOrder> findByDeletedFalse();
}
