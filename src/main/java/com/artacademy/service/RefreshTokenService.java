package com.artacademy.service;

import com.artacademy.entity.User;
import com.artacademy.model.RefreshToken;
import com.artacademy.repository.RefreshTokenRepository;
import com.artacademy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.artacademy.exception.InvalidRequestException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Refresh token service updated for MongoDB.
 * Uses userId as String reference instead of User object.
 */
@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return refreshTokenRepository.findByUserId(user.getId())
                .map(token -> {
                    token.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
                    token.setToken(UUID.randomUUID().toString());
                    return refreshTokenRepository.save(token);
                })
                .orElseGet(() -> {
                    RefreshToken newToken = RefreshToken.builder()
                            .userId(user.getId())
                            .token(UUID.randomUUID().toString())
                            .expiryDate(Instant.now().plusMillis(refreshExpiration))
                            .build();
                    return refreshTokenRepository.save(newToken);
                });
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new InvalidRequestException(
                    token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void deleteByUserId(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public void deleteByUsername(String email) {
        userRepository.findByEmailAndDeletedFalse(email)
                .ifPresent(user -> refreshTokenRepository.deleteByUserId(user.getId()));
    }
}