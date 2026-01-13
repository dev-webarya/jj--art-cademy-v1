package com.artacademy.controller;

import com.artacademy.dto.request.ReviewRequestDto;
import com.artacademy.dto.response.ReviewResponseDto;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.impl.ReviewServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    // Adding a review requires authentication (default from
    // .anyRequest().authenticated())
    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(@Valid @RequestBody ReviewRequestDto request) {
        log.info("Adding review for product: {} with rating: {}", request.getProductId(), request.getRating());
        return new ResponseEntity<>(reviewService.addReview(request), HttpStatus.CREATED);
    }

    // Public endpoint to read reviews
    @GetMapping("/product/{productId}")
    @PublicEndpoint
    public ResponseEntity<Page<ReviewResponseDto>> getProductReviews(
            @PathVariable UUID productId,
            Pageable pageable) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId, pageable));
    }
}