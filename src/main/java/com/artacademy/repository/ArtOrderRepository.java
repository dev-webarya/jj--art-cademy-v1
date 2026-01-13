package com.artacademy.repository;

import com.artacademy.entity.ArtOrder;
import com.artacademy.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtOrderRepository extends JpaRepository<ArtOrder, UUID>, JpaSpecificationExecutor<ArtOrder> {
    Optional<ArtOrder> findByOrderNumber(String orderNumber);

    Page<ArtOrder> findByUserId(UUID userId, Pageable pageable);

    List<ArtOrder> findByUserIdAndStatus(UUID userId, OrderStatus status);
}
