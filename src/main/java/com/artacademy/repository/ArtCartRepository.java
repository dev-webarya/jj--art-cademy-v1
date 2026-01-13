package com.artacademy.repository;

import com.artacademy.entity.ArtShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtCartRepository extends JpaRepository<ArtShoppingCart, UUID> {
    Optional<ArtShoppingCart> findByUserId(UUID userId);
}
