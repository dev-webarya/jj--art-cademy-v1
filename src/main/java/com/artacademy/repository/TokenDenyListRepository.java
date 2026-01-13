package com.artacademy.repository;

import com.artacademy.model.TokenDenyList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;

public interface TokenDenyListRepository extends JpaRepository<TokenDenyList, Long> {
    boolean existsByJti(String jti);

    void deleteByExpiryDateBefore(Instant now);
}