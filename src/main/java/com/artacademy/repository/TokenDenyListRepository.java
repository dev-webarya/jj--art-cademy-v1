package com.artacademy.repository;

import com.artacademy.model.TokenDenyList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface TokenDenyListRepository extends MongoRepository<TokenDenyList, String> {

    boolean existsByJti(String jti);

    void deleteByExpiryDateBefore(Instant now);
}