package com.artacademy.repository;

import com.artacademy.entity.ArtShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtCartRepository extends MongoRepository<ArtShoppingCart, String> {

    Optional<ArtShoppingCart> findByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}
